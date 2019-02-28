package org.cache.simple.worker;

import java.util.List;

import org.cache.simple.consts.NullConst;
import org.cache.simple.globle.GlobleContext;
import org.cache.simple.invoker.CacheInvoker;
import org.cache.simple.invoker.CacheJob;
import org.cache.simple.serializer.HessianSerializer;
import org.cache.simple.serializer.Serializer;
import org.cache.simple.utils.HashUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhaode
 * @date 2018年7月30日
 * 
 */
public class CacheWorker {

	private static final Logger log = LoggerFactory.getLogger(CacheWorker.class);

	
	@Autowired
	private CacheInvoker cacheInvoker;
	@Autowired(required = false)
	private Serializer serializer = new HessianSerializer();

	public CacheWorker() {
		super();
		GlobleContext.setCacheWorker(this);
	}


	/**
	 * @Title: deleteByClassAndMethodName
	 * @param target     目标class
	 * @param methodName 方法名称
	 * @param key        方法参数
	 * @return
	 */
	public Long deleteByClassAndMethodName(Class<?> target, String methodName, String key) {
		String prekey = getPreKey(target, methodName, key);
		log.info("[Clear] Cache For [prefixBizKey:" + prekey + ",And Keys:" + key + "]");
		return getCache().delete(prekey, key);
	}



	public <T> T invoke(String prefixBizKey, String key, Class<?> returnType, CacheJob<T> job) throws Throwable {
		Long startTime = System.currentTimeMillis();
		try {
			T result = null;
			byte[] obj = invokeByCache(prefixBizKey, key);
			if (obj != null) {
				log.info("[Hit] By Cache For [prefixBizKey:" + prefixBizKey + ",key:" + key + "]");
				if (NullConst.AssertNull(obj)) {
					return null;
				}
				result = (T) serializer.deserialize(obj, returnType);
				return result;
			}
			result = (T) invokeSelf(job);
			if (isSave(result)) {
				addToCache(prefixBizKey, key, result);
			}
			return result;
		} finally {
			Long endTimeDec = System.currentTimeMillis() - startTime;
			if (endTimeDec >= 800) {
				log.info("Task for prefixBizKey:" + prefixBizKey + ",key:" + key + " run for [" + endTimeDec + " ms]");
			}
		}

	}

	private static <T> T invokeSelf(CacheJob<T> job) throws Throwable {
		return job.invoke();
	}

	private byte[] invokeByCache(String prefixBizKey, String key) {
		return getCache().get(prefixBizKey, key);
	}

	public void addToCache(String prefixBizKey, String key, Object value) throws Exception {
		log.info("[Save] Cache by [prefixBizKey:" + prefixBizKey + ",key:" + key + "]");
		if (value == null) {
			getCache().put(prefixBizKey, key, NullConst.nullByte);
			return;
		}
		getCache().put(prefixBizKey, key, serializer.serialize(value));
	}

	private CacheInvoker getCache() {
		return cacheInvoker;
	};


	public static String getPreKey(Class<?> target, String methodName, String keys) {

		return target.getName() + ":" + methodName + ":" + HashUtil.hash(keys, keysCount);
	}



	private static Boolean isSave(Object obj) {
		Boolean status = GlobleContext.getAllowCacheStatus();
		if (status == null) {
			// defaltCondition
			if (obj != null) {
				if (obj instanceof List && ((List) obj).isEmpty()) {
					return false;
				}
				return true;
			}
			return false;
		} else {
			return status;
		}
	}

	// 2的10次
	@Autowired(required = false)
	private static int keysCount = 1024;


	public Serializer getSerializer() {
		return serializer;
	}

	public void setSerializer(Serializer serializer) {
		this.serializer = serializer;
	}

	public static int getKeysCount() {
		return keysCount;
	}

	public static void setKeysCount(int keysCount) {
		CacheWorker.keysCount = keysCount;
	}

}
