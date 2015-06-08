package com.everhomes.search;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import net.greghaines.jesque.Job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.everhomes.taskqueue.CommonWorkerPool;
import com.everhomes.taskqueue.JesqueClientFactory;

@Service
public class SearchSyncManagerImpl implements SearchSyncManager {
    @Autowired
    CommonWorkerPool workerPool;
    
    @Autowired
    JesqueClientFactory jesqueClientFactory;
    
    private String queueName = "search-sync";
    
    @PostConstruct
    public void setup() {
        workerPool.addQueue(queueName);
    }

    @Override
    public void SyncDb(SearchSyncType syncType) {
        final Job job = new Job(SearchSyncAction.class.getName(),
                new Object[]{ syncType.getCode() });
        
        jesqueClientFactory.getClientPool().enqueue(queueName, job);
    }

}
