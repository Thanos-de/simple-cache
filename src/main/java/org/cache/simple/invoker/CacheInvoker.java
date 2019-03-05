package org.cache.simple.invoker;

/**
 * @author zhaode
 * @date 2018年7月30日
 * 
 */
public interface CacheInvoker {

	boolean put(String prefixBizKey, String key, byte[] value);

	boolean delete(String prefixBizKey, String key);

	boolean delete(String prefixBizKey);

	byte[] get(String prefixBizKey, String key);
}
