package org.cache.simple.spring.intercept;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
public class ProceedingJoinPointOperator extends CacheOperator {

	@Pointcut("(@annotation(org.cache.simple.annotation.Cacheable) || @annotation(org.cache.simple.annotation.CacheRemove))")
	private void pointcut() {
	}

	/**
	 * 环绕通知
	 * 
	 * @throws Throwable
	 */
	@Around("pointcut()")
	public Object aroundMethod(final ProceedingJoinPoint joinPoint) throws Throwable {
		return oprateMethod(new Point() {

			@Override
			public Object proceed() throws Throwable {
				return joinPoint.proceed();
			}

			@Override
			public Object getThis() {
				return joinPoint.getTarget();
			}

			@Override
			public Method getMethod() {
				MethodSignature signature = (MethodSignature) joinPoint.getSignature();
				Method method = signature.getMethod();
				return method;
			}

			@Override
			public Object[] getArguments() {
				return joinPoint.getArgs();
			}
		});
	}
}
