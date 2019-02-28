package org.cache.simple.worker;

import org.cache.simple.invoker.CacheInvoker;
import org.cache.simple.serializer.HessianSerializer;
import org.cache.simple.serializer.Serializer;
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
	/*
	 * @see com.yongche.merchant.utils.cache.CacheInvoker#put(java.lang.String,
	 * java.lang.String, java.lang.Object)
	 */
	@Override
	public Long put(String prefixBizKey, String key, byte[] value) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = getJedis(prefixBizKey);
			Long result = 0L;
			if (value != null) {
				jedis.hsetnx(prefixBizKey.getBytes(), key.getBytes(), value);
			}
			//jedis.expire(prefixBizKey, 3600);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return null;

	}

	/*
	 * @see
	 * com.yongche.merchant.utils.cache.CacheInvoker#delete(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public Long delete(String prefixBizKey, String... key) {
		if (prefixBizKey != null && key != null) {

			Jedis jedis = null;
			try {
				jedis = getJedis(prefixBizKey);
				return jedis.hdel(prefixBizKey, key);
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			} finally {
				if (jedis != null) {
					jedis.close();
				}
			}
		}
		return null;

	}

	/*
	 * @see
	 * com.yongche.merchant.utils.cache.CacheInvoker#delete(java.lang.String)
	 */
	@Override
	public Long delete(String prefixBizKey) {
		// TODO Auto-generated method stub
		if (prefixBizKey != null) {

			Jedis jedis = null;
			try {
				jedis = getJedis(prefixBizKey);
				return jedis.del(prefixBizKey);
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			} finally {
				if (jedis != null) {
					jedis.close();
				}
			}
		}
		return null;
	}

	/*
	 * @see com.yongche.merchant.utils.cache.CacheInvoker#get(java.lang.String,
	 * java.lang.String)
	 */
	public byte[] get(String prefixBizKey, String key) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		byte[] data = null;
		try {
			jedis = getJedis(prefixBizKey);
			data = jedis.hget(prefixBizKey.getBytes(), key.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
//		log.debug("[GET]byRedisCache...:" + "Result:" + obj);
		return data;
	}
	public abstract Jedis getJedis(String prefixBizKey);

}
