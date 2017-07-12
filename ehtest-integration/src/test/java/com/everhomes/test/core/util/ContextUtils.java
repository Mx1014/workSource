package com.everhomes.test.core.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;

@Component
public class ContextUtils implements ApplicationContextAware, EmbeddedValueResolverAware  {
	private static ApplicationContext context;
	private static StringValueResolver stringValueResolver;

	public static Object getBean(String beanName) {
		return context.getBean(beanName);
	}

	public static <T> T getBean(Class<T> requireType) {
		return context.getBean(requireType);
	}

	public static String getProperty(String key) {
		return stringValueResolver.resolveStringValue(key);
	}

	@Override
	public void setApplicationContext(ApplicationContext cxt) throws BeansException {
		context = cxt;
	}

	@Override
	public void setEmbeddedValueResolver(StringValueResolver resolver) {
		stringValueResolver = resolver;
	}
}
