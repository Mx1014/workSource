package com.everhomes.eureka;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(EurekaClientConfigBean.class)
@ConditionalOnProperty(value = "eureka.client.enabled", matchIfMissing = true)
public class EurekaClientAutoConfiguration {


    // @Bean
    // public EurekaClientConfigBean eurekaClientConfigBean() {
    //
    //     return new EurekaClientConfigBean();
    // }

}
