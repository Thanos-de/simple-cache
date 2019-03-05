package org.cache.simple.spring.schemas;
import org.cache.simple.invoker.CacheInvoker;
import org.cache.simple.serializer.Serializer;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

public class SimpleCacheDefParser extends AbstractSingleBeanDefinitionParser {
	protected Class<SimpleCacheBean> getBeanClass(Element element) {
		return SimpleCacheBean.class;
	}

	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		String cacheInvoker = element.getAttribute("cacheInvoker");
		String serializer = element.getAttribute("serializer");
		
		
		if (StringUtils.hasText(serializer)) {
			try {
				Serializer ser = (Serializer) Class.forName(serializer).newInstance();
				builder.addPropertyValue("serializer", ser);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				throw new IllegalArgumentException(" class not found " + serializer);
			}
		}
		if (StringUtils.hasText(cacheInvoker)) {
			try {
				CacheInvoker cacheInv = (CacheInvoker) Class.forName(cacheInvoker).newInstance();
				builder.addPropertyValue("cacheInvoker", cacheInv);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				throw new IllegalArgumentException(" class not found " + cacheInvoker);
			}
		}
	}
}