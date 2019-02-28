package org.cache.simple.globle;

import org.cache.simple.worker.CacheWorker;

public class GlobleContext {

	private static CacheWorker cacheWorker;

	private static ThreadLocal<Boolean> saveStatus = new ThreadLocal<Boolean>();

	public static CacheWorker getCacheWorker() {
		return cacheWorker;
	}

	public static void setCacheWorker(CacheWorker cacheWorker) {
		GlobleContext.cacheWorker = cacheWorker;
	}

	public static Boolean getAllowCacheStatus() {
		Boolean status = saveStatus.get();
		if (status != null) {
			saveStatus.remove();
		}
		return status;
	}

	public static void setAllowCacheStatus(Boolean allow) {
		if (allow != null) {
			saveStatus.set(allow);
		}
	}

}
