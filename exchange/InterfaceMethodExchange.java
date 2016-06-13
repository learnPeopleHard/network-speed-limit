package com.yeepay.g3.core.o2o.utils.limit.exchange;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.google.common.base.Joiner;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.yeepay.g3.core.o2o.utils.limit.request.LimiterVO;

public class InterfaceMethodExchange {
	private static Logger logger = Logger.getLogger(InterfaceMethodExchange.class);
	private static LoadingCache<LimiterVO,BusiType> cahceBuilder;
	public int exchange(LimiterVO lVo){
		return Joiner.on("").skipNulls().join(lVo.getInterfaceName(),lVo.getMethodName()).hashCode();
	}
	public boolean isWorry(DBCall<BusiType> db,LimiterVO lVo){
		BusiType wor_or_non =getBusiType(db,lVo);
		if(BusiType.WORRY.equals(wor_or_non)) return true;
		return false;
	}
	
	private BusiType getBusiType(final DBCall<BusiType> db,LimiterVO lVo){
		if(cahceBuilder==null){
			synchronized (db) {
				if(cahceBuilder==null){
					cahceBuilder=CacheBuilder.newBuilder()
							.maximumSize(1000)
							.softValues()
							.expireAfterAccess(5, TimeUnit.MINUTES)
							.build(new CacheLoader<LimiterVO,BusiType >(){

						@Override
						public BusiType load(LimiterVO lvo) throws Exception {
							return db.run(lvo);
						}
						
					});
				}
			}
		}
		
		try {
			return cahceBuilder.get(lVo);
		} catch (ExecutionException e) {
			return BusiType.WORRY;
		}
	}
}
