package com.everhomes.pmtask;

import com.everhomes.rest.pmtask.CreateTaskCommand;
import com.everhomes.rest.pmtask.PmTaskDTO;

public interface PmTaskHandle {
	 String PMTASK_PREFIX = "Pmtask-";
	 String SHEN_YE = "shenye";
	 String FLOW = "flow";
	 String TECHPARK_REDIS_KEY_PREFIX = "techparkSynchronizedData-pmtask";
	 
	 PmTaskDTO createTask(CreateTaskCommand cmd, Long userId, String requestorName, String requestorPhone);
}
