package com.everhomes.queue.taskqueue;

import net.greghaines.jesque.worker.WorkerPool;

public interface WorkerPoolFactory {
    WorkerPool getWorkerPool();
}
