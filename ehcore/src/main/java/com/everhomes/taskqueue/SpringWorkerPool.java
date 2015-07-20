package com.everhomes.taskqueue;

import java.util.concurrent.Callable;
import java.util.concurrent.ThreadFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import net.greghaines.jesque.worker.Worker;
import net.greghaines.jesque.worker.WorkerPool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpringWorkerPool extends WorkerPool {

    private Logger logger = LoggerFactory.getLogger(SpringWorkerPool.class);

    public SpringWorkerPool(Callable<? extends Worker> workerFactory, int numWorkers) {
        super(workerFactory, numWorkers);
    }

    public SpringWorkerPool(Callable<? extends Worker> workerFactory, int numWorkers, ThreadFactory threadFactory) {
        super(workerFactory, numWorkers, threadFactory);
    }

    @PostConstruct
    public void init() {
        logger.info("Start a new thread for SpringWorkerPool");
        // new Thread(this).start();
    }

    @PreDestroy
    public void destroy() {
        logger.info("End the SpringWorkerPool thread");
        end(true);
    }
}
