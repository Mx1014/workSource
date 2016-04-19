package com.everhomes.aclink;

import javax.annotation.PostConstruct;

import net.greghaines.jesque.Job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.queue.taskqueue.JesqueClientFactory;
import com.everhomes.queue.taskqueue.WorkerPoolFactory;
import com.everhomes.rest.aclink.DoorMessage;
import com.everhomes.sequence.SequenceProvider;

@Component
public class AclinkMessageSequenceImpl implements AclinkMessageSequence {
    @Autowired
    private SequenceProvider sequenceProvider;
    
    @Autowired
    WorkerPoolFactory workerPoolFactory;
    
    @Autowired
    JesqueClientFactory jesqueClientFactory;
    
    private String queueName = "alcinkmessage";
    
    @PostConstruct
    public void setup() {
        workerPoolFactory.getWorkerPool().addQueue(queueName);
    }

    @Override
    public void pendingMessage(DoorMessage msg) {
        if(msg.getSeq() <= 0) {
        return;    
        }
        
        final Job job = new Job(DoorCommandAction.class.getName(),
                new Object[]{String.valueOf(msg.getSeq())});
        
        //5min
        jesqueClientFactory.getClientPool().delayedEnqueue(queueName, job, System.currentTimeMillis() + 5*60*1000);
        
        //DoorCommandAction daction = new DoorCommandAction(String.valueOf(seq), );
    }
    
    
    @Override
    public void ackMessage(Long seq) {
        if(seq <= 0) {
            return;
        }
        
        final Job job = new Job(DoorCommandAction.class.getName(),
                new Object[]{String.valueOf(seq)});
        
        jesqueClientFactory.getClientPool().removeDelayedEnqueue(queueName, job);
    }

}
