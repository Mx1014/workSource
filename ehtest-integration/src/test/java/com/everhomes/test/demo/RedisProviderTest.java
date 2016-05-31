// @formatter:off
package com.everhomes.test.demo;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.test.core.BaseServerTestCase;
import com.everhomes.test.core.redis.RedisProvider;

public class RedisProviderTest extends BaseServerTestCase {
    
    @Autowired
    private RedisProvider redisProvider;
    
    /**
     * <p>清除redis缓存</p>
     */
    @Ignore @Test
    public void redisCacheFlushall() {
        redisProvider.redisCacheFlushAll();
    }
    

    /**
     * <p>清除redis storage，要谨慎操作，清除后要手工重启服务器并同步sequence</p>
     */
    @Test
    public void redisStorageFlushall() {
        redisProvider.redisStorageFlushAll();
    }
}

