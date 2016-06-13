package com.yeepay.g3.core.o2o.utils.limit.config;

import java.util.Locale;
import java.util.ResourceBundle;

import com.yeepay.g3.utils.common.StringUtils;

public class ResourceConfig {
	private static ResourceBundle resourceBundle = null;
	
	static {
		Locale locale1 = new Locale("zh", "CN");  
        resourceBundle = ResourceBundle.getBundle("limiter-config",locale1);
	}
	
	public static int getSysMaxRunningThread(){
		String value = "";
    	try{
    		value = resourceBundle.getString("sys_max_running_thread");
    	 } catch (java.util.MissingResourceException exp) {
         }

         if (StringUtils.isEmpty(value)) {
        	 value = "500";
         }
         return Integer.parseInt(value);
	}
	public static int getSysMaxQPS(){
		String value = "";
		try{
			value = resourceBundle.getString("sys_max_qps");
		} catch (java.util.MissingResourceException exp) {
		}
		
		if (StringUtils.isEmpty(value)) {
			value = "250";
		}
		return Integer.parseInt(value);
	}
	public static int getRequestTimeout(){
		String value = "";
		try{
			value = resourceBundle.getString("request_timeout");
		} catch (java.util.MissingResourceException exp) {
		}
		
		if (StringUtils.isEmpty(value)) {
			value = "2000";
		}
		return Integer.parseInt(value);
	}
	public static int getNonWorryTokenSpeed(){
		String value = "";
		try{
			value = resourceBundle.getString("non_worry_token_speed");
		} catch (java.util.MissingResourceException exp) {
		}
		
		if (StringUtils.isEmpty(value)) {
			value = "30";
		}
		return Integer.parseInt(value);
	}
	public static int getMaxAwaitThreadLength(){
		String value = "";
		try{
			value = resourceBundle.getString("max_await_thread_length");
		} catch (java.util.MissingResourceException exp) {
		}
		
		if (StringUtils.isEmpty(value)) {
			value = "500";
		}
		return Integer.parseInt(value);
	}
	
	/**
	 * 最大平均处理时间，在没有开启限流时，流量变大，线程执行时间变长，
	 * 执行平均时间就会变长，那么这个时间就是平均下来，大概多长就需要开启限流了
	 * @return
	 */
	public static int getMaxAvgHandleDate(){
		String value = "";
		try{
			value = resourceBundle.getString("max_avg_handle_date");
		} catch (java.util.MissingResourceException exp) {
		}
		
		if (StringUtils.isEmpty(value)) {
			value = "4000";
		}
		return Integer.parseInt(value);
	}
	
	
}
