package org.cache.simple.worker;

import java.util.List;

import org.cache.simple.consts.NullConst;
import org.cache.simple.excutor.CacheExecutor;
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
	public boolean deleteByClassAndMethodName(Class<?> target, String methodName, String key) {
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
				return (T) handlerCacheResult(obj, returnType);
			}

			result = (T) invokeSelf(job);

			addToCache(prefixBizKey, key, result);
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

	public void addToCache(final String prefixBizKey, final String key, Object value) throws Exception {
		if (isSave(value)) {
			log.info("[Save] Cache by [prefixBizKey:" + prefixBizKey + ",key:" + key + "]");
			final byte[] bv;
			if (value == null) {
				bv = NullConst.nullByte;
			} else {
				bv = serializer.serialize(value);
			}
			CacheExecutor.commit(new Runnable() {

				@Override
				public void run() {
					try {
						if (getCache().put(prefixBizKey, key, bv)) {
							log.info("[Asyn Save success] Cache by [prefixBizKey:" + prefixBizKey + ",key:" + key + "]");
						}else {
							log.error("[Asyn Save error] Cache by [prefixBizKey:" + prefixBizKey + ",key:" + key + "]");
						}
					} catch (Exception e) {
						e.printStackTrace();
						log.error("[Asyn Save error] Cache by [prefixBizKey:" + prefixBizKey + ",key:" + key + "]");
					}
				}
			});

		}
	}

	private CacheInvoker getCache() {
		return cacheInvoker;
	};

	public  String getPreKey(Class<?> target, String methodName, String keys) {

		return target.getName() + ":" + methodName + ":" + HashUtil.hash(keys, keysCount);
	}

	private Object handlerCacheResult(byte[] obj, Class<?> returnType) throws Exception {
		if (NullConst.AssertNull(obj)) {
			return null;
		} else {
			return serializer.deserialize(obj, returnType);
		}
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
	private int keysCount = 1024;

	public Serializer getSerializer() {
		return serializer;
	}

	public void setSerializer(Serializer serializer) {
		this.serializer = serializer;
	}

	public int getKeysCount() {
		return keysCount;
	}

	public void setKeysCount(int keysCount) {
		this.keysCount = keysCount;
	}

	public CacheInvoker getCacheInvoker() {
		return cacheInvoker;
	}

	public void setCacheInvoker(CacheInvoker cacheInvoker) {
		this.cacheInvoker = cacheInvoker;
	}
	

}
