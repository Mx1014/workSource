package com.everhomes.util;

import com.everhomes.portal.PortalUrlParser;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

public class PortalUrlParserBeanUtil implements ApplicationContextAware {
    private static Map<String, PortalUrlParser> urlParserBeanMap;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, PortalUrlParser> map = applicationContext.getBeansOfType(PortalUrlParser.class);
        urlParserBeanMap = new HashMap<>();
        map.forEach((key, value) -> urlParserBeanMap.put(key, value));
    }

    public static <T extends PortalUrlParser> T getUrlParserBeanMap(String name) {
        return (T)urlParserBeanMap.get(name);
    }

    public static <T extends PortalUrlParser> T getkeys() {
        return (T)urlParserBeanMap.keySet();
    }
}
