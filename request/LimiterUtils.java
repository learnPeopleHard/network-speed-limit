package com.yeepay.g3.core.o2o.utils.limit.request;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import com.yeepay.g3.core.o2o.utils.limit.config.LimiterConfig;
import com.yeepay.g3.core.o2o.utils.limit.exchange.BusiType;
import com.yeepay.g3.core.o2o.utils.limit.exchange.DBCall;
import com.yeepay.g3.core.o2o.utils.limit.exchange.DefaultDBCall;
import com.yeepay.g3.core.o2o.utils.limit.switches.LimiterSwitch;
import com.yeepay.g3.core.o2o.utils.limit.token.TokenFactory;

public class LimiterUtils {
	private static Logger logger = Logger.getLogger(LimiterUtils.class);
	private static int current_await_thread_length =  0;
	
	private static Lock lock= new ReentrantLock();
	
	/**
	 * 模板方法
	 * 
	 * @param rc
	 * @return
	 */
	public static <T> T call(LimiterCall<T> rc,DBCall<BusiType> db,LimiterVO lVo) {
		long start_date =System.currentTimeMillis();
		LimiterSwitch limiter_switch = new LimiterSwitch();
		limiter_switch.addCurrentRunningThread();
		if(db==null){
			db =new DefaultDBCall();
		}
		boolean flag_suc = false;
		try {
			if(getInterfaceLimiter(limiter_switch, db,lVo)){
				flag_suc = true;
				return rc.success();
			}
			return rc.fail();
		}catch(Exception e){
			logger.error("获取令牌异常", e);
			return rc.fail();
		} finally {
			long end_date =System.currentTimeMillis();
			limiter_switch.removeCurrentRunningThread();
			if(flag_suc) limiter_switch.addRunningOverTime(end_date-start_date);
			limiter_switch.judgeLimiterSwitch();
			System.out.println("当前线程执行时间："+(end_date-start_date)+",获取锁："+flag_suc+",当前拥有正在执行线程"+limiter_switch.getCurrentRunningThread()+",当前QPS："+limiter_switch.getCurrent_QPS()+",当前平均执行时间："+limiter_switch.getCurrentAvgHandleDate());
		}

	}
	
	
	private static boolean getInterfaceLimiter(LimiterSwitch limiter_switch ,DBCall<BusiType> db,LimiterVO lVo){
		logger.info("interfaceName:"+lVo.getInterfaceName()+",methodName:"+lVo.getMethodName()+" 开始进入限流判断");
		try {
			if(!limiter_switch.isLimiterSwitch()){
				logger.info("开关没有打开,不用限制流量,当前拥有正在执行线程"+limiter_switch.getCurrentRunningThread());
				return true;
			}
			logger.info("开关已经打开,开始限制流量");
			long get_token_date = System.currentTimeMillis();
			if(TokenFactory.getToken(db, lVo)) {
				logger.info("获取锁的使用时间是："+(System.currentTimeMillis()-get_token_date));
				return true;
				}
			if(current_await_thread_length<=LimiterConfig.getMaxAwaitThreadLength()){
				logger.info("没有获取到令牌，当前等待线程为："+current_await_thread_length+"，需要等待");
				try {
					lock.lock();
					current_await_thread_length++;
				} finally{
					lock.unlock();
				}
				long await_start_date = System.currentTimeMillis();
				boolean await_token_flag = false;
				while(current_await_thread_length<=LimiterConfig.getMaxAwaitThreadLength()){
					if(System.currentTimeMillis()-await_start_date>5000) break;
					if(TokenFactory.getToken(db, lVo)) await_token_flag =true;
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
					}
				}
				if(await_token_flag){
					try {
						lock.lock();
						current_await_thread_length--;
					} finally{
						lock.unlock();
					}
					logger.info("等待线程获取到了令牌，还有"+current_await_thread_length+"线程等待");
					return true;
				}
				
			}
			return false;
		} finally{
			
		}
	}
}
