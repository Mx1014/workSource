// @formatter:off
package com.everhomes.questionnaire;

import com.everhomes.queue.taskqueue.JesqueClientFactory;
import com.everhomes.queue.taskqueue.WorkerPoolFactory;
import com.everhomes.yellowPage.ServiceAllianceAsynchronizedAction;
import net.greghaines.jesque.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 
 *  @author:dengs
 */
@Component("questionnaireAsynchronizedServiceImpl")
public class QuestionnaireAsynchronizedServiceImpl implements ApplicationListener<ContextRefreshedEvent>{


	private static final Logger LOGGER = LoggerFactory.getLogger(QuestionnaireAsynchronizedServiceImpl.class);

	@Autowired
    WorkerPoolFactory workerPoolFactory;
    
    @Autowired
    JesqueClientFactory jesqueClientFactory;
	
    private String questionnaireTasks = "questionnaireAsynchronizedSendMessages";
    
	void pushToQueque(Long questionnaireId) {
		
		final Job job = new Job(QuestionnaireAsynchronizedAction.class.getName(), new Object[]{String.valueOf(questionnaireId)});
		
		jesqueClientFactory.getClientPool().enqueue(questionnaireTasks, job);
		
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		if(null == event.getApplicationContext().getParent()) {
	        workerPoolFactory.getWorkerPool().addQueue(questionnaireTasks);

		}
		
	}

	


}
