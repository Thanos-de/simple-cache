package org.cache.simple.spring.intercept;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class MethodInvocationOperator extends CacheOperator implements MethodInterceptor {

	@Override
	public Object invoke(final MethodInvocation methodInvocation) throws Throwable {

		return oprateMethod(new Point() {

			@Override
			public Object proceed() throws Throwable {
				return methodInvocation.proceed();
			}

			@Override
			public Object getThis() {
				return methodInvocation.getThis();
			}

			@Override
			public Method getMethod() {
				return methodInvocation.getMethod();
			}

			@Override
			public Object[] getArguments() {
				return methodInvocation.getArguments();
			}
		});
	}

}
