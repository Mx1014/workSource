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

@Component("techparkSynchronizedServiceImpl")
class TechparkSynchronizedServiceImpl implements ApplicationListener<ContextRefreshedEvent>{

	private static final Logger LOGGER = LoggerFactory.getLogger(TechparkSynchronizedServiceImpl.class);

	@Autowired
    WorkerPoolFactory workerPoolFactory;
    
    @Autowired
    JesqueClientFactory jesqueClientFactory;
	
    private String pmtask = "synchronizedpmtask";
    
	void pushToQueque(String json) {
		
		final Job job = new Job(TechparkSynchronizedAction.class.getName(), new Object[]{ json });
		
		jesqueClientFactory.getClientPool().enqueue(pmtask, job);
		
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		if(null == event.getApplicationContext().getParent()) {
	        workerPoolFactory.getWorkerPool().addQueue(pmtask);

		}
		
	}

	

}
