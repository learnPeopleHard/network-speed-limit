# network-speed-limit
接口或者url限流
请求来了判断一下是否需要限流，不需要就正常走，
记录一下时间，需要的话，就去获取令牌，获取到了就执行，
没有就等待一下，如果等待超时就返回，等待中获取到了令牌就执行，
量很多，令牌桶不够用了，就限制一下不是很着急的那些个请求，保证着急的请求先执行。

当然了，非着急的接口或者url也可以使用SemaPhore，这样限制性会更强，我这里只是使用了比较简单的方式，就新建一个RateLimiter

DEMO：
		LimiterVO lVo = new LimiterVO("OrderOperateFacade","buyGoodsRequest");
		LimiterUtils.call(new LimiterCall<Boolean>() {

			@Override
			public Boolean success() {
				try {
					int slp=0;
					if(i<10000){
						slp=(int)(Math.random()*2+1)*1000;
					}else if(i>=10000 && i<50000){
						slp=(int)(Math.random()*5+3)*1000;
					}else{
						slp=(int)(Math.random()*3+1)*1000;
					}
					
					Thread.sleep(slp);
					System.out.println("hu获得令牌了,并执行了");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return true;
			}

			@Override
			public Boolean fail() {
				System.out.println("没有获得令牌了");
				return false;
			}
			
		}, new DBCall<BusiType>() {

			@Override
			public BusiType run(LimiterVO lvo) {
				System.out.println("开始执行DB操作");
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
				}
				System.out.println("结束执行DB操作");
				return BusiType.WORRY;
			}
			
		},lVo);
