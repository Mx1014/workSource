package com.everhomes.sms;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Component;

/**
 * 管理短信发送，如果宕机是不是需要存储信息?
 * 
 * @author elians
 *
 */
@Component
public class TaskQueue {
    private ExecutorService service;

    @PostConstruct
    public void init() {
        service = Executors.newFixedThreadPool(2);
    }

    @PreDestroy
    public void destroy() {
        if (service != null)
            service.shutdown();
    }

    public Future<?> submit(Callable<?> task) {
        // need cache task or not?
        return service.submit(task);
    }
}
