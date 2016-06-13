package com.yeepay.g3.core.o2o.utils.limit.exchange;

import com.yeepay.g3.core.o2o.utils.limit.request.LimiterVO;

public class DefaultDBCall implements DBCall<BusiType> {

	@Override
	public BusiType run(LimiterVO lvo) {
		return BusiType.WORRY;
	}

}
