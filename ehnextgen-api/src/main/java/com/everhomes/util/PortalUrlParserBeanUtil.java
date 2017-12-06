package com.everhomes.util;

import com.everhomes.portal.PortalUrlParser;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PortalUrlParserBeanUtil implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    private static ConcurrentHashMap<String, PortalUrlParser> urlParserBeanMap;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        Map<String, PortalUrlParser> map = this.applicationContext.getBeansOfType(PortalUrlParser.class);
        urlParserBeanMap = new ConcurrentHashMap();
        map.forEach((key, value) -> urlParserBeanMap.put(key, value));
    }

    public static <T extends PortalUrlParser> T getUrlParserBeanMap(String name) {
        return (T) urlParserBeanMap.get(name);
    }

    public static Set<String> getkeys() {
        if (urlParserBeanMap != null) {
            return urlParserBeanMap.keySet();
        } else {
            return null;
        }
    }
}
