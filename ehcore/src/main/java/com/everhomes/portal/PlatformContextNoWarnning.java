package com.everhomes.portal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2018/4/24.
 */
@Component
public class PlatformContextNoWarnning implements ApplicationContextAware {

        private static final Logger LOGGER = LoggerFactory.getLogger(PlatformContextNoWarnning.class);
        private static PlatformContextNoWarnning s_instance;
        private ApplicationContext applicationContext;

        public PlatformContextNoWarnning() {
            s_instance = this;
        }

        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
        }

        public static <T> T getComponent(Class<T> clz) {
            try {
                return s_instance.applicationContext.getBean(clz);
            } catch (Exception var2) {
                LOGGER.warn("Unexpected exception, get bean fail. Class={}", clz.getName());
                return null;
            }
        }

        public static <T> T getComponent(String name) {
            try {
                return (T)s_instance.applicationContext.getBean(name);
            } catch (Exception var2) {
                LOGGER.warn("Unexpected exception, get bean fail. name={}", name);
                return null;
            }
        }
}
