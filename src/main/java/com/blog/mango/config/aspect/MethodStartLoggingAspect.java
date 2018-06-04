package com.blog.mango.config.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.blog.mango.user.exception.UserAlreadyRegisteredException;

//@Aspect // 컴포넌트가 에스펙트로 식별되도록 에너테이션을 적용한다
@Component("loggingAspect") // DI 컨테이너에서 관리되도록 에너테이션을 적용한다
public class MethodStartLoggingAspect {
//	@Before("inServiceLayer()")
	public void startLog(JoinPoint joinPoint) {
		Object targetObject = joinPoint.getTarget();
		Object thisObject = joinPoint.getThis();
		Object[] args = joinPoint.getArgs();
		
		System.out.println("인스턴스 원본: "+ targetObject);
		System.out.println("프락시: "+ thisObject);
		System.out.println("----- 인수들 -----");
		for(Object arg: args) System.out.println("인수: "+ arg);
	}
}