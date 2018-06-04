package com.blog.mango.config.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class NamedPointCuts {
	@Pointcut("execution(* com.blog.mango.user.service.*ServiceImpl.*(..))")
	public void inServiceLayer() {}
	
	@Pointcut("execution(* com.blog.mango.user.repository.*Repository+.*(..))")
	public void inRepositoryLayer() {}
	
	@Pointcut("execution(* *..*Encoder+.*(..))")
	public void inEncoderLayer() {}
}
