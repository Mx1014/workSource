package com.everhomes.taskqueue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

import javax.annotation.PostConstruct;

import net.greghaines.jesque.Config;
import net.greghaines.jesque.ConfigBuilder;
import net.greghaines.jesque.worker.WorkerImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringWorkerFactory implements Callable<WorkerImpl>, ApplicationContextAware {

    private Logger logger = LoggerFactory.getLogger(SpringWorkerFactory.class);
    private Config config;
    private final Collection<String> queues = new ArrayList<String>();
    
    @Value("${redis.store.master.host}")
    private String redisHost;
    
    @Value("${redis.store.master.port}")
    private int redisPort;
    
    @Autowired
    private ApplicationContext applicationContext;
    
    @PostConstruct
    public void setup() {
        this.config = new ConfigBuilder().withHost(redisHost).withPort(redisPort).withNamespace("everhomesjes").build();
    }

    @Override
    public WorkerImpl call() {
        logger.info("Create new Spring Worker");
        WorkerImpl springWorker = new SpringWorker(this.config, this.queues);
        ((SpringWorker) springWorker).setApplicationContext(this.applicationContext);
        return springWorker;
    }
    
    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
