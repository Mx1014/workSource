package com.everhomes.search;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import net.greghaines.jesque.Job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import com.everhomes.taskqueue.CommonWorkerPool;
import com.everhomes.taskqueue.JesqueClientFactoryImpl;

@Service
public class SearchSyncManagerImpl implements SearchSyncManager, ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    CommonWorkerPool workerPool;
    
    @Autowired
    JesqueClientFactoryImpl jesqueClientFactory;
    
    private String queueName = "search-sync";
    
    // 升级平台包到1.0.1，把@PostConstruct换成ApplicationListener，
    // 因为PostConstruct存在着平台PlatformContext.getComponent()会有空指针问题 by lqs 20180516
    //@PostConstruct
    public void setup() {
        workerPool.addQueue(queueName);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null) {
            setup();
        }
    }
    
    @Override
    public void SyncDb(SearchSyncType syncType) {
        final Job job = new Job(SearchSyncAction.class.getName(),
                new Object[]{ syncType.getCode() });
        
        jesqueClientFactory.getClientPool().enqueue(queueName, job);
    }

}
