package org.cache.simple.globle;

import org.aopalliance.intercept.MethodInvocation;

public class GlobleThreadLocal {
	private final static ThreadLocal<MethodInvocation> currentMethodInvocation = new ThreadLocal<MethodInvocation>();

	public static MethodInvocation getCurrentmethodinvocation() {
		return currentMethodInvocation.get();
	}
	
}
