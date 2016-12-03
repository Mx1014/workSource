package com.everhomes.flow;

import javax.annotation.PostConstruct;

import net.greghaines.jesque.Job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.pushmessage.PushMessageAction;
import com.everhomes.queue.taskqueue.JesqueClientFactory;
import com.everhomes.queue.taskqueue.WorkerPoolFactory;

@Component
public class FlowTimeoutServiceImpl implements FlowTimeoutService {
    @Autowired
    WorkerPoolFactory workerPoolFactory;
    
    @Autowired
    JesqueClientFactory jesqueClientFactory;
    
	@Autowired
	FlowTimeoutProvider flowTimeoutProvider;
    
    private String queueName = "flowtimeouts";
    
    @PostConstruct
    public void setup() {
        workerPoolFactory.getWorkerPool().addQueue(queueName);
    }
    
    @Override
    public void pushTimeout(FlowTimeout ft) {
    	//FlowTimeoutAction
    	flowTimeoutProvider.createFlowTimeout(ft);
    	
    	final Job job = new Job(PushMessageAction.class.getName(), new Object[]{String.valueOf(ft.getId()) });
    	jesqueClientFactory.getClientPool().delayedEnqueue(queueName, job, ft.getTimeoutTick().getTime());
    }
    
    @Override
    public void processTimeout(FlowTimeout ft) {
    }
}
