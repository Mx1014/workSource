// @formatter:off
package com.everhomes.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.everhomes.queue.taskqueue.JesqueClientFactory;
import com.everhomes.queue.taskqueue.WorkerPoolFactory;
import com.everhomes.rest.activity.ActivityCancelSignupCommand;
import com.everhomes.rest.activity.ActivityCancelType;
import com.everhomes.rest.activity.ActivityRosterPayFlag;
import com.everhomes.rest.activity.ActivityRosterStatus;

import net.greghaines.jesque.Job;

@Component
public class RosterPayTimeoutServiceImpl implements RosterPayTimeoutService, ApplicationListener<ContextRefreshedEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(RosterPayTimeoutServiceImpl.class);
	
    @Autowired
    WorkerPoolFactory workerPoolFactory;
    
    @Autowired
    JesqueClientFactory jesqueClientFactory;
    
    @Autowired
    private ActivityService activityService;
    
    @Autowired
    private ActivityProivider activityProvider;
    
    private String queueDelay = "rosterpaydelays";
    private String queueNoDelay = "rosterpaynodelays";
    
    private void setup() {
        workerPoolFactory.getWorkerPool().addQueue(queueDelay);
        workerPoolFactory.getWorkerPool().addQueue(queueNoDelay);
    }
    
	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		setup();
	}
    
    @Override
    public void pushTimeout(ActivityRoster roster) {
    	if(roster != null && roster.getOrderExpireTime() != null) {
    		final Job job = new Job(RosterPayTimeoutAction.class.getName(), new Object[]{String.valueOf(roster.getId()) });
    		if(roster.getOrderExpireTime().getTime() > (System.currentTimeMillis()+10l) ) {
    			jesqueClientFactory.getClientPool().delayedEnqueue(queueDelay, job, roster.getOrderExpireTime().getTime());	
    		} else {
    			jesqueClientFactory.getClientPool().enqueue(queueNoDelay, job);
    		}
        		
    	} else {
    		LOGGER.error("create RosterPayTimeout error! rosterId=" + roster.toString());
    	}
    }
    
    @Override
    public void cancelTimeoutOrder(Long rosterId) {

    	ActivityRoster roster = activityProvider.findRosterById(rosterId);
    	if(roster == null ){
    		LOGGER.error("Roster not found error! rosterId=" + rosterId);
    		return;
    	}
    	
    	if(roster.getStatus() == null || roster.getStatus() != ActivityRosterStatus.NORMAL.getCode()){
    		LOGGER.warn("Roster status is innormal! do not need cancel. rosterId=" + rosterId);
    		return;
    	}
    	
    	if(roster.getPayFlag() == null || roster.getPayFlag().byteValue() == ActivityRosterPayFlag.UNPAY.getCode()){
    		ActivityCancelSignupCommand cmd = new ActivityCancelSignupCommand();
        	cmd.setActivityId(roster.getActivityId());
        	cmd.setUserId(roster.getUid());
        	cmd.setCancelType(ActivityCancelType.EXPIRE_AUTO.getCode());
        	activityService.cancelSignup(cmd);
    	}
    }
}
