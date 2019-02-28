package org.cache.globle;

import org.cache.simple.worker.CacheWorker;

public class GlobleContext {
	private static CacheWorker cacheWorker;
	
	
	public static CacheWorker getCacheWorker() {
		return cacheWorker;
	}

	public static void setCacheWorker(CacheWorker cacheWorker) {
		GlobleContext.cacheWorker = cacheWorker;
	}
	
	
}
