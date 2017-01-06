package com.everhomes.pmtask;


import net.greghaines.jesque.Job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.everhomes.queue.taskqueue.JesqueClientFactory;
import com.everhomes.queue.taskqueue.WorkerPoolFactory;

@Component("pmtaskTechparkHandler")
public class PmtaskTechparkHandler implements ApplicationListener<ContextRefreshedEvent>{

	private static final Logger LOGGER = LoggerFactory.getLogger(PmtaskTechparkHandler.class);

	@Autowired
    WorkerPoolFactory workerPoolFactory;
    
    @Autowired
    JesqueClientFactory jesqueClientFactory;
	
    private String pmtask = "synchronizedpmtask";
    
	public void pushToQueque(Long taskId) {
		
		final Job job = new Job(TechparkSynchronizedData.class.getName(), new Object[]{ String.valueOf(taskId) });
		
		jesqueClientFactory.getClientPool().enqueue(pmtask, job);
		
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		if(null == event.getApplicationContext().getParent()) {
	        workerPoolFactory.getWorkerPool().addQueue(pmtask);

		}
		
	}

	

}
