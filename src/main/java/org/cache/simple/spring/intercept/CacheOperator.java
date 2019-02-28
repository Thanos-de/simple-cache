package org.cache.simple.spring.intercept;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.annotation.Resource;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.cache.simple.annotation.CacheRemove;
import org.cache.simple.annotation.Cacheable;
import org.cache.simple.invoker.CacheJob;
import org.cache.simple.utils.ArgUtil;
import org.cache.simple.utils.ExpressionUtil;
import org.cache.simple.worker.CacheWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhaode
 * @date 2018年9月5日 aop 缓存拦截
 */
public class CacheOperator implements MethodInterceptor {

	private static Logger logger = LoggerFactory.getLogger(CacheOperator.class);
	@Resource
	CacheWorker cacheWorker;

	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		return oprateMethod(methodInvocation);
	}

	public Object oprateMethod(MethodInvocation methodInvocation) throws Throwable {

		Method method = methodInvocation.getMethod();
		Cacheable cacheable = method.getAnnotation(Cacheable.class);
		CacheRemove cacheRemove = method.getAnnotation(CacheRemove.class);

		operateRemove(methodInvocation, method, cacheRemove);
		return operateCache(methodInvocation, method, cacheable);
	}

	// 处理移除缓存
	private void operateRemove(MethodInvocation methodInvocation, Method method, CacheRemove cacheRemove) {
		if (cacheRemove == null ||ExpressionUtil.isBlank(cacheRemove.expression())
				|| cacheRemove.methodName() == null || cacheRemove.methodName().trim().equals("")) {
			return;
		}

		Class tclass = cacheRemove.targetClass();
		if (tclass == CacheRemove.class) {
			tclass = methodInvocation.getThis().getClass();
		}
		String methodName = cacheRemove.methodName();
		String key;
		try {
			key = ExpressionUtil.getKeyByExpressionAndParams(methodInvocation.getArguments(),
					cacheRemove.expression());
			for (String name : methodName.split(",")) {
				cacheWorker.deleteByClassAndMethodName(tclass, name, key);
			}
		} catch (Exception e) {
			logger.warn("cacheRemove fail!", e);
		}
		
	}

	// 处理缓存
	private Object operateCache(final MethodInvocation methodInvocation, Method method, Cacheable cacheable)
			throws Throwable {

		boolean isAllNull = false;
		if (cacheable != null && !cacheable.canAllNull()) {
			isAllNull = isAllNull(methodInvocation.getArguments());
		}

		if (cacheable == null || ExpressionUtil.isBlank(cacheable.expression())
				|| method.getReturnType().toString().equals("void") || isAllNull) {
			return methodInvocation.proceed();
		}

		String key = ExpressionUtil.getKeyByExpressionAndParams(methodInvocation.getArguments(),
				cacheable.expression());

		Class<?> returnType = method.getReturnType();

		return cacheWorker.invoke(CacheWorker.getPreKey(methodInvocation.getThis().getClass(), method.getName(), key),
				key, returnType, new CacheJob<Object>() {
					@Override
					public Object invoke() throws Throwable {
						return methodInvocation.proceed();
					}
				});
	}

	private boolean isAllNull(Object[] keys) {
		for (Object key : keys) {
			if (key != null) {
				return false;
			}
		}
		return true;
	}
}
