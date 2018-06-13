package com.everhomes.taskqueue;

import java.util.concurrent.Callable;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import net.greghaines.jesque.worker.Worker;

@Component
public class CommonWorkerPool extends SpringWorkerPool {

    // 升级平台包到1.0.1，把@PostConstruct换成ApplicationListener，
    // 因为PostConstruct存在着平台PlatformContext.getComponent()会有空指针问题
    // 由于目前接口没有实现内容，故没有加上ApplicationListener，如果有实现请加上 by lqs 20180516
    //@PostConstruct
    public void setup() {
        //this.run();
    }
    
    @Autowired(required = true)
    public CommonWorkerPool(@Qualifier(value="workerFactory") Callable<? extends Worker> workerFactory) {
        super(workerFactory, 3);
    }

}
