package com.everhomes.junit;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.everhomes.sharding.Server;
import com.everhomes.sharding.ServerStatus;
import com.everhomes.sharding.ServerType;
import com.everhomes.sharding.ShardingProvider;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(initializers = { PropertyInitializer.class },
    loader = AnnotationConfigContextLoader.class)
public class ConfigInitializer {

    @Autowired
    private ShardingProvider shardingProvider;

    @Configuration
    @ComponentScan(basePackages = {
        "com.everhomes"
    })
    @EnableAutoConfiguration(exclude={
            DataSourceAutoConfiguration.class, 
            HibernateJpaAutoConfiguration.class,
        })
    static class ContextConfiguration {
    }

    @Ignore @Test
    public void setupServerConfig() {
        Server server;
        
        server = new Server();
        server.setAddressUri("192.168.1.223");
        server.setAddressPort(6379);
        server.setServerType(ServerType.RedisStorage.ordinal());
        server.setStatus(ServerStatus.enabled.ordinal());
        shardingProvider.createServer(server);
        
        server = new Server();
        server.setAddressUri("192.168.1.223");
        server.setAddressPort(6379);
        server.setServerType(ServerType.RedisCache.ordinal());
        server.setStatus(ServerStatus.enabled.ordinal());
        shardingProvider.createServer(server);
    }
}
