package com.yeepay.g3.core.o2o.utils.limit.exchange;

import com.yeepay.g3.core.o2o.utils.limit.request.LimiterVO;

public interface DBCall<T> {
	/**
	 * 执行DB操作
	 * @param lvo
	 * @return
	 */
	public T run(LimiterVO lvo);
}
