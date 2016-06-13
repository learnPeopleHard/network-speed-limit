package com.yeepay.g3.core.o2o.utils.limit.token;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.google.common.util.concurrent.RateLimiter;
import com.yeepay.g3.core.o2o.utils.limit.config.LimiterConfig;
import com.yeepay.g3.core.o2o.utils.limit.exchange.BusiType;
import com.yeepay.g3.core.o2o.utils.limit.exchange.DBCall;
import com.yeepay.g3.core.o2o.utils.limit.exchange.InterfaceMethodExchange;
import com.yeepay.g3.core.o2o.utils.limit.request.LimiterVO;

public class TokenFactory {
	private static Logger logger = Logger.getLogger(TokenFactory.class);
	private static RateLimiter worryLimiter;
	
	//当前使用令牌来尝试一下，然后再用SemaPhore来尝试
	private static RateLimiter nonWorryLimiter;
	
	static {
		if(notExistInit())
			createToken();
	}
	
	public static void createToken(){
		worryLimiter = RateLimiter.create(LimiterConfig.getMaxQPS());
	}
	public static double getTokenRate(){
		return worryLimiter.getRate();
	}
	public static boolean notExistInit(){
		if(worryLimiter==null) return true;
		if(worryLimiter.getRate()<=0) return true;
		return false;
	}
	public static boolean notExistNonWorry(){
		if(nonWorryLimiter==null) return true;
		if(nonWorryLimiter.getRate()<=0) return true;
		return false;
	}
	
	public static boolean getToken(DBCall<BusiType> db,LimiterVO lVo){
		if(notExistNonWorry()){
			if(worryLimiter.tryAcquire(LimiterConfig.getReuqestTimeout(), TimeUnit.MILLISECONDS)){
				logger.info("非着急限流没有打开，且获得令牌了");
				return true;
			}
			logger.info("着急限流已经没有令牌了，需要对非着急接口进行限制，开启非着急限流");
			if(!notExistInit() && notExistNonWorry()){
				synchronized (TokenFactory.class) {
					if(!notExistInit() && notExistNonWorry())
						nonWorryLimiter =  RateLimiter.create(LimiterConfig.getNonWorryTokenSpeed());
				}
			}
			return false;
		}
		InterfaceMethodExchange exchange = new InterfaceMethodExchange();
		if(exchange.isWorry(db,lVo)){
			if(worryLimiter.tryAcquire(LimiterConfig.getReuqestTimeout(), TimeUnit.MILLISECONDS)) return true;
			return false;
		}
		
		if(nonWorryLimiter.tryAcquire(LimiterConfig.getReuqestTimeout(), TimeUnit.MILLISECONDS)) return true;
		return false;
	}
}	
