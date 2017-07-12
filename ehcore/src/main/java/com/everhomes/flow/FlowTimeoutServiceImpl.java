package com.everhomes.flow;

import com.everhomes.flow.action.FlowTimeoutJob;
import com.everhomes.queue.taskqueue.JesqueClientFactory;
import com.everhomes.queue.taskqueue.WorkerPoolFactory;
import com.everhomes.rest.flow.FlowTimeoutType;
import com.everhomes.scheduler.ScheduleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    @Autowired
    private ScheduleProvider scheduleProvider;
    
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
    
    /*@Override
    public void pushTimeoutByJesque(FlowTimeout ft) {
    	//FlowTimeoutAction
    	flowTimeoutProvider.createFlowTimeout(ft);
    	
    	if(ft.getId() > 0) {
    		final Job job = new Job(FlowTimeoutAction.class.getName(), new Object[]{String.valueOf(ft.getId()) });
    		if(ft.getTimeoutTick().getTime() > (System.currentTimeMillis() + 10L)) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("pushTimeout delayedEnqueue ft = {}", ft);
                jesqueClientFactory.getClientPool().delayedEnqueue(queueDelay, job, ft.getTimeoutTick().getTime());
    		} else {
    			jesqueClientFactory.getClientPool().enqueue(queueNoDelay, job);
    		}
    	} else {
    		LOGGER.error("create flowTimeout error! ft=" + ft.toString());
    	}
    }*/

    @Override
    public void pushTimeout(FlowTimeout ft) {
    	//FlowTimeoutAction
    	flowTimeoutProvider.createFlowTimeout(ft);

    	if (ft.getId() > 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("flowTimeoutId", ft.getId());
            // map.put("ctx", ctx);

            if (ft.getTimeoutTick().getTime() > (System.currentTimeMillis() + 10L)) {
                scheduleProvider.scheduleSimpleJob(
                        queueDelay + ft.getId(),
                        queueDelay + ft.getId(),
                        new Date(ft.getTimeoutTick().getTime()),
                        FlowTimeoutJob.class,
                        map
                );

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("pushTimeout delayedEnqueue ft = {}", ft);
                }
            } else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("pushTimeout enqueue ft = {}", ft);
                }
                scheduleProvider.scheduleSimpleJob(
                        queueNoDelay + ft.getId(),
                        queueNoDelay + ft.getId(),
                        new Date(System.currentTimeMillis() + 1000),
                        FlowTimeoutJob.class,
                        map
                );
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
    		break;
    	case SMS_TIMEOUT:
    		flowService.processSMSTimeout(ft);
    		break;
    	default:
    		break;
    	}
    }
}
