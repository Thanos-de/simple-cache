package org.cache.simple.spring.schemas;


import org.cache.simple.invoker.CacheInvoker;
import org.cache.simple.serializer.Serializer;
import org.cache.simple.spring.intercept.ProceedingJoinPointOperator;
import org.cache.simple.worker.CacheWorker;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class SimpleCacheBean
		implements FactoryBean<ProceedingJoinPointOperator>,BeanFactoryPostProcessor{

	private CacheInvoker cacheInvoker;

	private Serializer serializer;


	@Override
	public ProceedingJoinPointOperator getObject() throws Exception {
		ProceedingJoinPointOperator proceedingJoinPointOperator = new ProceedingJoinPointOperator();
		CacheWorker cw = new CacheWorker();
		if (serializer != null) {
			cw.setSerializer(serializer);
		}
		cw.setCacheInvoker(cacheInvoker);
		proceedingJoinPointOperator.setCacheWorker(cw);
		return proceedingJoinPointOperator;
	}

	@Override
	public Class<?> getObjectType() {
		return ProceedingJoinPointOperator.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public CacheInvoker getCacheInvoker() {
		return cacheInvoker;
	}

	public void setCacheInvoker(CacheInvoker cacheInvoker) {
		this.cacheInvoker = cacheInvoker;
	}

	public Serializer getSerializer() {
		return serializer;
	}

	public void setSerializer(Serializer serializer) {
		this.serializer = serializer;
	}


	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		
	}


}
