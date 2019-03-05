package org.cache.simple.worker;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.cache.simple.invoker.CacheInvoker;

public class ConcurrentHashMapInvoker implements CacheInvoker{
	
	private Map<String, byte[]> map = new ConcurrentHashMap<String, byte[]>();
	@Override
	public boolean put(String prefixBizKey, String key, byte[] value) {
		map.put(prefixBizKey+":"+key, value);
		return true;
	}

	@Override
	public boolean delete(String prefixBizKey, String key) {
		map.remove(prefixBizKey+":"+key);
		return true;
	}

	@Override
	public boolean delete(String prefixBizKey) {
		throw new RuntimeException("not support!");
	}

	@Override
	public byte[] get(String prefixBizKey, String key) {
		return map.get(prefixBizKey+":"+key);
	}

}
