package com.yeepay.g3.core.o2o.utils.limit.switches;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.google.common.collect.Ordering;
import com.yeepay.g3.core.o2o.utils.limit.config.LimiterConfig;

public class LimiterSwitch {
	
	private static List<Long> lastRunningOverTimeList ;
	
	private static boolean limiterSwitch =false;
	
	//private static String current_running_thread_point = "curr_run_thread_point";
	
	private static int current_QPS = 0;
	private static long current_avg_handle_date = 0l;
	
	private static Long  current_running_thread_length = 0l;
	
	private Lock lock= new ReentrantLock();
	
	private Lock runninglock= new ReentrantLock();
	
	public Long addCurrentRunningThread(){
		runninglock.lock();
		try{
			current_running_thread_length++;
		}finally{
			runninglock.unlock();
		}
		return current_running_thread_length;
	}
	
	public Long getCurrentRunningThread(){
		return current_running_thread_length;
	}
	
	public Long removeCurrentRunningThread(){
		runninglock.lock();
		try{
			if(current_running_thread_length>0){
				current_running_thread_length--;
			}
		}finally{
			runninglock.unlock();
		}
		return current_running_thread_length;
	}

	public void addRunningOverTime(Long runTime){
		if(lastRunningOverTimeList==null){
			synchronized (LimiterSwitch.class) {
				if(lastRunningOverTimeList==null){
					lastRunningOverTimeList = Collections.synchronizedList(new LinkedList<Long>());
				}
			}
		}
		if(lastRunningOverTimeList.size()>=100){
			lastRunningOverTimeList.remove(0);
		}
		lastRunningOverTimeList.add(runTime);
		int lastOverSize = lastRunningOverTimeList.size();
		if(lastOverSize<50) return;
		int _10_pre =lastOverSize/10;
		if(_10_pre==0) return;
		Ordering<Long> order = Ordering.natural();
		List<Long> sortList  = order.sortedCopy(lastRunningOverTimeList);
		long sumTime = 0;
		for(int i=0;i<sortList.size();i++){
			if(i<=_10_pre || i>=lastOverSize-_10_pre) continue;
			sumTime+=sortList.get(i);
		}
		current_avg_handle_date = sumTime/sortList.size();
		current_QPS =(int)((getCurrentRunningThread()*1000)/current_avg_handle_date);
	}
	
	public  boolean isLimiterSwitch() {
		return limiterSwitch;
	}

	public int getCurrent_QPS() {
		return current_QPS;
	}
	
	public long getCurrentAvgHandleDate(){
		return current_avg_handle_date;
	}

	public boolean judgeLimiterSwitch() {
		if(!limiterSwitch &&  getCurrentRunningThread()>LimiterConfig.getMaxRunningThread()
				&& current_avg_handle_date >LimiterConfig.getMaxAvgHandleDate()){
			limiterSwitch = true;
			return limiterSwitch;
		}
		if(limiterSwitch &&  getCurrentRunningThread()<LimiterConfig.getMaxRunningThread()
				&& current_avg_handle_date <LimiterConfig.getMaxAvgHandleDate()){
			limiterSwitch = false;
			return limiterSwitch;
		}
		return limiterSwitch;
	}
	
}
