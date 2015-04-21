// @formatter:off
package com.everhomes.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import com.everhomes.bus.CoreBusReceiver;

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
    
    //
    // TODO : currently only support single Redis server, will take care of sharding later
    // 
    @Value("${redis.bus.host}")
    String redisServerHost;
    
    @Value("${redis.bus.port}")
    int redisServerPort;
    
    @Value("${upload.max.size}")
    long maxUploadSize;
    
    //
    // Redis configuration
    //
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic("core-bus"));

        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(CoreBusReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }
    
    @Bean
    StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }
    
    @Bean
    CoreBusReceiver coreBusReceiver() {
        return new CoreBusReceiver();
    }
    
    @Bean
    RedisConnectionFactory connectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        
        LOGGER.info("Using Redis server " + this.redisServerHost + ":" + this.redisServerPort);
        factory.setHostName(this.redisServerHost);
        factory.setPort(this.redisServerPort);
        return factory;
    }
    
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
}
