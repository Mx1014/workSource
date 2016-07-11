package com.everhomes.queue.taskqueue;

import net.greghaines.jesque.client.ClientPoolImpl;

public interface JesqueClientFactory {
    ClientPoolImpl getClientPool();
}
