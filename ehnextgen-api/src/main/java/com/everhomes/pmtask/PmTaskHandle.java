package com.everhomes.pmtask;

import java.util.List;

import com.everhomes.rest.category.CategoryDTO;
import com.everhomes.rest.pmtask.CancelTaskCommand;
import com.everhomes.rest.pmtask.CreateTaskCommand;
import com.everhomes.rest.pmtask.EvaluateTaskCommand;
import com.everhomes.rest.pmtask.GetTaskDetailCommand;
import com.everhomes.rest.pmtask.ListAllTaskCategoriesCommand;
import com.everhomes.rest.pmtask.ListTaskCategoriesCommand;
import com.everhomes.rest.pmtask.ListTaskCategoriesResponse;
import com.everhomes.rest.pmtask.ListUserTasksCommand;
import com.everhomes.rest.pmtask.ListUserTasksResponse;
import com.everhomes.rest.pmtask.PmTaskDTO;
import com.everhomes.rest.pmtask.SearchTasksCommand;
import com.everhomes.rest.pmtask.SearchTasksResponse;

public interface PmTaskHandle {
	 long EBEI_TASK_CATEGORY = 1l;
	 String PMTASK_PREFIX = "Pmtask-";
	 String SHEN_YE = "shenye";
	 String EBEI = "ebei";
	 String FLOW = "flow";
	 String TECHPARK_REDIS_KEY_PREFIX = "techparkSynchronizedData-pmtask";
	 
	 PmTaskDTO createTask(CreateTaskCommand cmd, Long userId, String requestorName, String requestorPhone);
	 
	 void cancelTask(CancelTaskCommand cmd);
	 
	 void evaluateTask(EvaluateTaskCommand cmd);
	 
	 PmTaskDTO getTaskDetail(GetTaskDetailCommand cmd);
	 
	 ListTaskCategoriesResponse listTaskCategories(ListTaskCategoriesCommand cmd);
	 
	 List<CategoryDTO> listAllTaskCategories(ListAllTaskCategoriesCommand cmd);
	 
	 SearchTasksResponse searchTasks(SearchTasksCommand cmd);
	 
	 ListUserTasksResponse listUserTasks(ListUserTasksCommand cmd);
}
