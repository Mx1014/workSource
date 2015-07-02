package com.everhomes.taskqueue;

import javax.annotation.PostConstruct;

import net.greghaines.jesque.Config;
import net.greghaines.jesque.ConfigBuilder;
import net.greghaines.jesque.client.ClientPoolImpl;
import net.greghaines.jesque.utils.PoolUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.everhomes.queue.taskqueue.JesqueClientFactory;

@Component
public class JesqueClientFactoryImpl implements JesqueClientFactory {
    
    private ClientPoolImpl jesqueClientPool;
    private Config config;
    
    @Value("${redis.store.master.host}")
    private String redisHost;
    
    @Value("${redis.store.master.port}")
    private int redisPort;
    
    @PostConstruct
    public void setup() {
        config = new ConfigBuilder().withHost(redisHost).withPort(redisPort).withNamespace("everhomesjes").build();
        
        jesqueClientPool = new ClientPoolImpl(config, PoolUtils.createJedisPool(config));
    }
    
    public Config getConfig() {
        return config;
    }
    
    public ClientPoolImpl getClientPool() {
        return this.jesqueClientPool;
    }
}
