package com.everhomes.pmtask;

import java.util.List;

import com.everhomes.rest.category.CategoryDTO;
import com.everhomes.rest.pmtask.*;

public interface PmTaskHandle {
	 long EBEI_TASK_CATEGORY = 1l;
	 String PMTASK_PREFIX = "Pmtask-";
	 String SHEN_YE = "shenye";
	 String EBEI = "ebei";
	 String FLOW = "flow";
	 String TECHPARK_REDIS_KEY_PREFIX = "techparkSynchronizedData-pmtask";
	 
	 PmTaskDTO createTask(CreateTaskCommand cmd, Long requestorUid, String requestorName, String requestorPhone);
	 
	 void cancelTask(CancelTaskCommand cmd);
	 
	 void evaluateTask(EvaluateTaskCommand cmd);
	 
	 PmTaskDTO getTaskDetail(GetTaskDetailCommand cmd);
	 
	 ListTaskCategoriesResponse listTaskCategories(ListTaskCategoriesCommand cmd);
	 
	 List<CategoryDTO> listAllTaskCategories(ListAllTaskCategoriesCommand cmd);
	 
	 SearchTasksResponse searchTasks(SearchTasksCommand cmd);
	 
	 ListUserTasksResponse listUserTasks(ListUserTasksCommand cmd);

	void updateTaskByOrg(UpdateTaskCommand cmd);
}
