// @formatter:off
package com.everhomes.yellowPage;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.everhomes.general_approval.GeneralApproval;
import com.everhomes.queue.taskqueue.JesqueClientFactory;
import com.everhomes.queue.taskqueue.WorkerPoolFactory;

import net.greghaines.jesque.Job;

/**
 * 
 *  @author:dengs
 */
@Component("serviceAllianceAsynchronizedServiceImpl")
public class ServiceAllianceAsynchronizedServiceImpl implements ApplicationListener<ContextRefreshedEvent>{


	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceAllianceAsynchronizedServiceImpl.class);

	@Autowired
    WorkerPoolFactory workerPoolFactory;
    
    @Autowired
    JesqueClientFactory jesqueClientFactory;
	
    private String serviceAllianceTasks = "serviceAllianceSendEmailTasks";
    
	void pushToQueque(String contents,Long userId) {
		
		final Job job = new Job(ServiceAllianceAsynchronizedAction.class.getName(), new Object[]{contents,String.valueOf(userId)});
		
		jesqueClientFactory.getClientPool().enqueue(serviceAllianceTasks, job);
		
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		if(null == event.getApplicationContext().getParent()) {
	        workerPoolFactory.getWorkerPool().addQueue(serviceAllianceTasks);

		}
		
	}

	


}
