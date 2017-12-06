package com.everhomes.util;

import com.everhomes.portal.PortalUrlParser;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class PortalUrlParserBeanUtil implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    private static Map<String, PortalUrlParser> urlParserBeanMap;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        Map<String, PortalUrlParser> map = this.applicationContext.getBeansOfType(PortalUrlParser.class);
        urlParserBeanMap = new HashMap<>();
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
