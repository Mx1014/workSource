package com.everhomes.junit;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.mock.env.MockPropertySource;

/**
 * 
 * Provides mocked property environment for integration tests
 * 
 * @author Kelven Yang
 *
 */
public class PropertyInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    public void initialize(ConfigurableApplicationContext applicationContext) {
         applicationContext.getEnvironment().getPropertySources().addFirst(
           new MockPropertySource()
               .withProperty("redis.bus.host", "192.168.1.223")
            .withProperty("redis.bus.port", "6379")
            .withProperty("upload.max.size", 100000)
            .withProperty("messaging.msgbox.size", 10)
            .withProperty("messaging.batch.size", 10)
            .withProperty("forum.postbox.size", 10)
            .withProperty("forum.flush.interval", 3000)
            .withProperty("redis.sharding.strategy", "nosharding")
            .withProperty("db.master", "jdbc:mysql://ehcore:ehcore@127.0.0.1:3306/ehcore")
            .withProperty("db.conn.pool", 128)
            .withProperty("db.tx.timeout", 600)
            .withProperty("db.driver", "com.mysql.jdbc.Driver"));
    }
}
