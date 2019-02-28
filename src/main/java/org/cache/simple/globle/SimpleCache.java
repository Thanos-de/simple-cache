package org.cache.simple.globle;

import org.cache.simple.utils.ExpressionUtil;
import org.cache.simple.worker.CacheWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleCache {

	private static final Logger log = LoggerFactory.getLogger(SimpleCache.class);
	
	/**
	 *  显式移除缓存
	 *
	 * @param targetClass 缓存目标类
	 * @param methodName 方法名称
	 * @param expressoion 表达式
	 * @param methodParams 方法参数
	 * @return 
	 * @throws Exception
	 */
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

	/**
	 * 显式增加缓存
	 *
	 * @param targetClass 目标类
	 * @param methodName 方法名称
	 * @param value 缓存对象
	 * @param expressoion 表达式
	 * @param methodParams 方法参数
	 * @return
	 * @throws Exception
	 */
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

	/**
	 *缓存条件
	 *
	 * @param condition
	 */
	public void cacheCondition(Boolean condition) {
		GlobleContext.setAllowCacheStatus(condition);
	}
}
