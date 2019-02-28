package org.cache.simple.invoker;

/**
 * @author zhaode
 * @date 2018年7月30日
 * 
 */
public interface CacheInvoker {
	
	Long put(String prefixBizKey,String key, byte[] value);

	Long delete(String prefixBizKey,String... key);
	
	Long delete(String prefixBizKey);
	
	byte[]  get(String prefixBizKey,String key);
}
