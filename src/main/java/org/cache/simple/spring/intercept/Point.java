package org.cache.simple.spring.intercept;

import java.lang.reflect.Method;

public interface Point {
	
	Object proceed() throws Throwable;

	Object getThis();
	
	Method getMethod();

	Object[] getArguments();
	
}
