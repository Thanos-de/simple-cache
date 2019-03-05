package org.cache.simple.worker;

import org.cache.simple.invoker.CacheInvoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

/**
 * @author zhaode
 * @date 2018年7月31日
 * 
 */
public abstract class RedisCacheInvoker implements CacheInvoker {
	private static final Logger log = LoggerFactory.getLogger(RedisCacheInvoker.class);

	public static void main(String[] args) {

	}

	@Override
	public boolean put(String prefixBizKey, String key, byte[] value) {
		Jedis jedis = null;
		try {
			jedis = getJedis(prefixBizKey);
			if (value != null) {
				jedis.hsetnx(prefixBizKey.getBytes(), key.getBytes(), value);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return false;

	}

	@Override
	public boolean delete(String prefixBizKey, String key) {
		if (prefixBizKey != null && key != null) {

			Jedis jedis = null;
			try {
				jedis = getJedis(prefixBizKey);
				jedis.hdel(prefixBizKey, key);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (jedis != null) {
					jedis.close();
				}
			}
		}
		return false;

	}

	@Override
	public boolean delete(String prefixBizKey) {
		if (prefixBizKey != null) {

			Jedis jedis = null;
			try {
				jedis = getJedis(prefixBizKey);
				jedis.del(prefixBizKey);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (jedis != null) {
					jedis.close();
				}
			}
		}
		return false;
	}

	public byte[] get(String prefixBizKey, String key) {
		Jedis jedis = null;
		byte[] data = null;
		try {
			jedis = getJedis(prefixBizKey);
			data = jedis.hget(prefixBizKey.getBytes(), key.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return data;
	}

	public abstract Jedis getJedis(String prefixBizKey);

}
