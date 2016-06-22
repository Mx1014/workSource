package com.everhomes.test.core.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisProviderImpl implements RedisProvider {
    @Autowired
    RedisTemplate<String, String> redisCacheTemplate;
    
    @Autowired
    RedisTemplate<String, String> redisStorageTemplate;
    
    @Override
    public void redisCacheFlushAll() {
        redisCacheTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                connection.flushAll();
                return null;
            }
        });
    }
    
    @Override
    public void redisStorageFlushAll() {
        redisStorageTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                connection.flushAll();
                return null;
            }
        });
    }
}
