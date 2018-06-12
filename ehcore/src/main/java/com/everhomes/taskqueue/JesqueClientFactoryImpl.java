package com.everhomes.taskqueue;

import javax.annotation.PostConstruct;

import net.greghaines.jesque.Config;
import net.greghaines.jesque.ConfigBuilder;
import net.greghaines.jesque.client.ClientPoolImpl;
import net.greghaines.jesque.utils.PoolUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.everhomes.queue.taskqueue.JesqueClientFactory;

@Component
public class JesqueClientFactoryImpl implements JesqueClientFactory, ApplicationListener<ContextRefreshedEvent> {
    
    private ClientPoolImpl jesqueClientPool;
    private Config config;
    
    @Value("${redis.store.master.host}")
    private String redisHost;
    
    @Value("${redis.store.master.port}")
    private int redisPort;
    
    // 升级平台包到1.0.1，把@PostConstruct换成ApplicationListener，
    // 因为PostConstruct存在着平台PlatformContext.getComponent()会有空指针问题 by lqs 20180516
    //@PostConstruct
    public void setup() {
        config = new ConfigBuilder().withHost(redisHost).withPort(redisPort).withNamespace("everhomesjes").build();
        
        jesqueClientPool = new ClientPoolImpl(config, PoolUtils.createJedisPool(config));
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null) {
            setup();
        }
    }
    
    public Config getConfig() {
        return config;
    }
    
    public ClientPoolImpl getClientPool() {
        return this.jesqueClientPool;
    }
}
