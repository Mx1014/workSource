// @formatter:off
package com.everhomes.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

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
@ImportResource(value="classpath*:**/applicationContext.xml")
@EnableWebSocket
@EnableCaching
@EnableScheduling
public class AppConfig {
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
    
    @Bean
    CharacterEncodingFilter encodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }
    
//    @Bean
//    public SchedulerFactoryBean quartz() {
//        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
//        factoryBean.setAutoStartup(true);
//        factoryBean.setApplicationContextSchedulerContextKey("applicationContext");
//        return factoryBean;
//    }
    
}
