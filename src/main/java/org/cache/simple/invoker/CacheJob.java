package org.cache.simple.invoker;


/**
 * @author zhaode
 * @date 2018年7月30日 
 * 
 */
public interface CacheJob <T>  {
	 T invoke() throws Throwable ;
}
