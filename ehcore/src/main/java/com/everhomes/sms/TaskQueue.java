package com.everhomes.sms;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.elasticsearch.common.util.concurrent.ThreadFactoryBuilder;
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
    private ThreadFactory namedThreadFactory;
    
    @PostConstruct
    public void init() {
    	//added by Janson
    	namedThreadFactory = new ThreadFactoryBuilder()
        .setNameFormat("sms-thr-%d").build();
    	service = Executors.newFixedThreadPool(2, namedThreadFactory);
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
