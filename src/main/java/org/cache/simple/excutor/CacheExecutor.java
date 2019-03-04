package org.cache.simple.excutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CacheExecutor {
	
	private static ExecutorService cachePool = new ThreadPoolExecutor(20, 20, 30L, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>(300), new CacheThreadFactory("SimpleCache-", true, Thread.NORM_PRIORITY),
			new ThreadPoolExecutor.DiscardPolicy());
	
	public static void commit(Runnable job) {
		cachePool.execute(job);
	}
}
