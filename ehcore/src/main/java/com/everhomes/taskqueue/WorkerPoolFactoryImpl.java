package com.everhomes.taskqueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.greghaines.jesque.worker.WorkerPool;

import com.everhomes.queue.taskqueue.WorkerPoolFactory;

@Component
public class WorkerPoolFactoryImpl implements WorkerPoolFactory {

    @Autowired
    CommonWorkerPool commonWorkerPool;
    
    @Override
    public WorkerPool getWorkerPool() {
        return commonWorkerPool;
    }

}
