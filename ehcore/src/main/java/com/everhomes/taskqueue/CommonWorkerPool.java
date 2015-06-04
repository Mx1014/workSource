package com.everhomes.taskqueue;

import java.util.concurrent.Callable;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import net.greghaines.jesque.worker.Worker;

@Component
public class CommonWorkerPool extends SpringWorkerPool {

    @PostConstruct
    public void setup() {
        //this.run();
    }
    
    @Autowired(required = true)
    public CommonWorkerPool(@Qualifier(value="workerFactory") Callable<? extends Worker> workerFactory) {
        super(workerFactory, 1);
    }

}
