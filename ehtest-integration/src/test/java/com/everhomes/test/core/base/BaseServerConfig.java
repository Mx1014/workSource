// @formatter:off
package com.everhomes.test.core.base;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan(basePackages = {"com.everhomes.test"})
@PropertySource("classpath:config/ehcore.properties")
public class BaseServerConfig {
    /** 服务器context路径，即API前缀 */
    @Value("${server.contextPath}")
    private String serverContextPath;
    
    /** 服务器地址 */
    @Value("${server.address}")
    private String serverAddress;
    
//    @Bean
//    @Scope("prototype")
//    public HttpClientService getHttpClientService() {
//        HttpClientService service = new HttpClientServiceImpl();
//        service.setServerAddress(serverAddress);
//        service.setServerContextPath(serverContextPath);
//        
//        return service;
//    }
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
