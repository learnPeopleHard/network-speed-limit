# network-speed-limit
接口或者url限流
请求来了判断一下是否需要限流，不需要就正常走，
记录一下时间，需要的话，就去获取令牌，获取到了就执行，
没有就等待一下，如果等待超时就返回，等待中获取到了令牌就执行，
量很多，令牌桶不够用了，就限制一下不是很着急的那些个请求，保证着急的请求先执行。
