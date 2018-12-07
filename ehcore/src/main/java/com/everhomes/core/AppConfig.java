// @formatter:off
package com.everhomes.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

/**
 * Note
 *     We support a mixed-mode of bean configuration. You can both configure beans here
 *  or in applicationContext.xml.
 *
 *  In general, use annotation based configuration if you can, but if you do need 
 *  more expressive way in the configuration, put them in applicationContext.xml 
 *
 * @author Kelven Yang
 */
@Configuration
@ImportResource(value = "classpath*:**/applicationContext.xml")
@EnableWebSocket
@EnableCaching
@EnableAsync
@EnableScheduling
@EnableKafka
public class AppConfig implements AsyncConfigurer {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppConfig.class);

    public static int DEFAULT_PAGINATION_PAGE_SIZE = 20;

    @Value("${upload.max.size}")
    long maxUploadSize;
    
    /* 
     * According to the test: when try to upload a file, the parameter 'MultipartFile[]' will be empty 
     * if bean 'multipartResolver' is configured as bellow. So fail to upload the file and the reason is unclear yet. 
     * The file can be uploaded successfully after the below bean 'multipartResolver' is commented.
     * But it is not sure whether other module will be affected without the bean 'multipartResolver'.
     * By liangqishi 20150417
    @Bean
    CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setMaxUploadSize(maxUploadSize);
        resolver.setDefaultEncoding("utf-8");
        
        return resolver;
    }
    */

    // 使用下面的filter进行转码，反而使得不管是请求还是响应结果都中文乱码，把代码注释掉则不乱码，原因未明  by lqs 20180530
//    @Bean
//    CharacterEncodingFilter encodingFilter() {
//        CharacterEncodingFilter filter = new CharacterEncodingFilter();
//
//        filter.setEncoding("UTF-8");
//        filter.setForceEncoding(true);
//        return filter;
//    }

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(6);
        executor.setMaxPoolSize(128);
        executor.setThreadNamePrefix("AsyncMethodInvocation-");
        executor.initialize();
        return executor;
    }

//    @Bean
//    public SchedulerFactoryBean quartz() {
//        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
//        factoryBean.setAutoStartup(true);
//        factoryBean.setApplicationContextSchedulerContextKey("applicationContext");
//        return factoryBean;
//    }

    // 升级平台包到1.0.1时需要更新spring-boot版本，导致需要实现该方法 by lqs 20180526
    // AsyncConfigurer主要应用于使用@Async注解
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SpringAsyncExceptionHandler();
    }

    class SpringAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
        @Override
        public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
                    LOGGER.error("Exception occurs in async method {}", throwable);
                }
    }
}
