package org.cache.simple.globle;

import org.cache.simple.utils.ExpressionUtil;
import org.cache.simple.worker.CacheWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleCache {

	private static final Logger log = LoggerFactory.getLogger(SimpleCache.class);

	public static Boolean removeCache(Class targetClass, String methodName, String expressoion, Object... methodParams)
			throws Exception {
		try {
			String key = ExpressionUtil.getKeyByExpressionAndParams(methodParams, expressoion);
			GlobleContext.getCacheWorker().deleteByClassAndMethodName(targetClass, methodName, key);
		} catch (Exception e) {
			log.warn("delete cache error", e);
			return false;
		}
		return true;
	}

	public static Boolean putCache(Class targetClass, String methodName, Object value, String expressoion,
			Object... methodParams) throws Exception {
		try {
			String key = ExpressionUtil.getKeyByExpressionAndParams(methodParams, expressoion);
			CacheWorker cw = GlobleContext.getCacheWorker();
			cw.addToCache(CacheWorker.getPreKey(targetClass, methodName, key), key, value);
		} catch (Exception e) {
			log.warn("delete cache error", e);
			return false;
		}
		return true;
	}
}
