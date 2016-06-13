package com.yeepay.g3.core.o2o.utils.limit.config;

import java.util.Locale;
import java.util.ResourceBundle;

public class LimiterConfig {
	public static int sys_max_running_thread ;
	
	public static int sys_max_QPS ;
	
	public static int request_timeout ;
	
	
	public static int non_worry_token_speed ;
	
	public static int  max_await_thread_length;
	
	public static long  max_avg_handle_date;
	

	static {
		init();
	}
	public static void init(){
		if(existInit()) return ;
		sys_max_running_thread = ResourceConfig.getSysMaxRunningThread();
		sys_max_QPS = ResourceConfig.getSysMaxQPS();
		request_timeout = ResourceConfig.getRequestTimeout();
		non_worry_token_speed = ResourceConfig.getNonWorryTokenSpeed();
		max_await_thread_length = ResourceConfig.getMaxAwaitThreadLength();
		max_avg_handle_date = ResourceConfig.getMaxAvgHandleDate();
	} 
	
	public static boolean existInit(){
		if(sys_max_running_thread>0
	||sys_max_QPS>0) return true;
		return false;
	}
	
	public static int getMaxRunningThread(){
		return sys_max_running_thread;
	}
	public static int getMaxQPS(){
		return sys_max_QPS;
	}
	
	public static int getReuqestTimeout(){
		return request_timeout;
	}
	
	public static int getNonWorryTokenSpeed(){
		return non_worry_token_speed;
	}
	
	public static int getMaxAwaitThreadLength(){
		return max_await_thread_length;
	}
	public static long getMaxAvgHandleDate(){
		return max_avg_handle_date;
	}
	
	
}
