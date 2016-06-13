package com.everhomes.test.core.redis;

public interface RedisProvider {
    void redisCacheFlushAll();
    void redisStorageFlushAll();
}
