package org.cache.simple.spring.schemas;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class SimpleCacheHandler extends NamespaceHandlerSupport {
	public void init() {
		registerBeanDefinitionParser("cache", new SimpleCacheDefParser());
	}
}