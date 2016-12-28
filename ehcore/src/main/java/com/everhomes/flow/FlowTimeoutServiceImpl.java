package com.everhomes.flow;

import javax.annotation.PostConstruct;

import net.greghaines.jesque.Job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.everhomes.pushmessage.PushMessageAction;
import com.everhomes.queue.taskqueue.JesqueClientFactory;
import com.everhomes.queue.taskqueue.WorkerPoolFactory;
import com.everhomes.rest.flow.FlowTimeoutType;
import com.everhomes.util.DateHelper;

@Component
public class FlowTimeoutServiceImpl implements FlowTimeoutService, ApplicationListener<ContextRefreshedEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(FlowTimeoutServiceImpl.class);
	
    @Autowired
    WorkerPoolFactory workerPoolFactory;
    
    @Autowired
    JesqueClientFactory jesqueClientFactory;
    
	@Autowired
	FlowTimeoutProvider flowTimeoutProvider;
	
	@Autowired
	FlowService flowService;
    
    private String queueDelay = "flowdelays";
    private String queueNoDelay = "flownodelays";
    
    private void setup() {
        workerPoolFactory.getWorkerPool().addQueue(queueDelay);
        workerPoolFactory.getWorkerPool().addQueue(queueNoDelay);
    }
    
	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		setup();
	}
    
    @Override
    public void pushTimeout(FlowTimeout ft) {
    	//FlowTimeoutAction
    	flowTimeoutProvider.createFlowTimeout(ft);
    	
    	if(ft.getId() > 0) {
    		final Job job = new Job(FlowTimeoutAction.class.getName(), new Object[]{String.valueOf(ft.getId()) });
    		if(ft.getTimeoutTick().getTime() > (System.currentTimeMillis()+10l) ) {
    			jesqueClientFactory.getClientPool().delayedEnqueue(queueDelay, job, ft.getTimeoutTick().getTime());	
    		} else {
    			jesqueClientFactory.getClientPool().enqueue(queueNoDelay, job);
    		}
        		
    	} else {
    		LOGGER.error("create flowTimeout error! ft=" + ft.toString());
    	}
    }
    
    @Override
    public void processTimeout(FlowTimeout ft) {
    	FlowTimeoutType timeoutType = FlowTimeoutType.fromCode(ft.getTimeoutType());
    	switch(timeoutType) {
    	case STEP_TIMEOUT:
    		flowService.processStepTimeout(ft);
    		break;
    	case MESSAGE_TIMEOUT:
    		flowService.processMessageTimeout(ft);
    	default:
    		break;
    	}
    }
}
