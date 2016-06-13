package com.yeepay.g3.core.o2o.utils.limit.request;

public class LimiterVO {
	private String interfaceName;
	private String methodName;
	private String url;
	private LimiterType  limiterType;
	
	public LimiterVO(String interfaceName,String methodName){
		limiterType = LimiterType.INTERFACE;
		this.interfaceName = interfaceName;
		this.methodName = methodName;
	}
	public LimiterVO(String url){
		limiterType = LimiterType.URL;
		this.url = url;
	}
	public String getInterfaceName() {
		return interfaceName;
	}
	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public LimiterType getLimiterType() {
		return limiterType;
	}
	public void setLimiterType(LimiterType limiterType) {
		this.limiterType = limiterType;
	}
	
}
