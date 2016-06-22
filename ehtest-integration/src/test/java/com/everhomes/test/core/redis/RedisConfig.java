package com.everhomes.test.core.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@ComponentScan(basePackages = {"com.everhomes.test"})
@PropertySource("classpath:config/ehcore.properties")
public class RedisConfig {
    @Autowired
    private Environment env;
 
    @Bean
    public RedisTemplate<String, String> redisCacheTemplate() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<String, String>();
        redisTemplate.setConnectionFactory(redisCacheConnectionFactory());
        return redisTemplate;
    }
 
    @Bean
    public RedisTemplate<String, String> redisStorageTemplate() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<String, String>();
        redisTemplate.setConnectionFactory(redisStorageConnectionFactory());
        return redisTemplate;
    }
    
    @Bean
    public JedisConnectionFactory redisCacheConnectionFactory() {
        String host = env.getProperty("redis.cache.host");
        int port = Integer.parseInt(env.getProperty("redis.cache.port"));
        JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory();

        redisConnectionFactory.setHostName(host);
        redisConnectionFactory.setPort(port);
        return redisConnectionFactory;
    }
    
    @Bean
    public JedisConnectionFactory redisStorageConnectionFactory() {
        String host = env.getProperty("redis.storage.host");
        int port = Integer.parseInt(env.getProperty("redis.storage.port"));
        JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory();

        redisConnectionFactory.setHostName(host);
        redisConnectionFactory.setPort(port);
        return redisConnectionFactory;
    }
}
