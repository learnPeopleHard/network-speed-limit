package com.yeepay.g3.core.o2o.utils.limit.request;


public interface LimiterCall<T> {
	/**
	 * 操作
	 * 
	 * @param limiterVO
	 * @return
	 */
	public T success();
	/**
	 * 操作
	 * 
	 * @param limiterVO
	 * @return
	 */
	public T fail();
}
