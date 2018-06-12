package com.everhomes.taskqueue;

import java.util.concurrent.Callable;
import java.util.concurrent.ThreadFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import net.greghaines.jesque.worker.Worker;
import net.greghaines.jesque.worker.WorkerPool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class SpringWorkerPool extends WorkerPool implements ApplicationListener<ContextRefreshedEvent> {

    private Logger logger = LoggerFactory.getLogger(SpringWorkerPool.class);

    public SpringWorkerPool(Callable<? extends Worker> workerFactory, int numWorkers) {
        super(workerFactory, numWorkers);
    }

    public SpringWorkerPool(Callable<? extends Worker> workerFactory, int numWorkers, ThreadFactory threadFactory) {
        super(workerFactory, numWorkers, threadFactory);
    }
    
    // 升级平台包到1.0.1，把@PostConstruct换成ApplicationListener，
    // 因为PostConstruct存在着平台PlatformContext.getComponent()会有空指针问题 by lqs 20180516
    //@PostConstruct
    public void init() {
        logger.info("Start a new thread for SpringWorkerPool");
        new Thread(this).start();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null) {
            init();
        }
    }
    
    @PreDestroy
    public void destroy() {
        logger.info("End the SpringWorkerPool thread");
        end(true);
    }
}
