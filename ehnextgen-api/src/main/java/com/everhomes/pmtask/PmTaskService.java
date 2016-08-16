package com.everhomes.pmtask;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.everhomes.rest.pmtask.AssignTaskCommand;
import com.everhomes.rest.pmtask.CommunityTaskDTO;
import com.everhomes.rest.pmtask.CreateCategoryCommand;
import com.everhomes.rest.pmtask.CreateNewTaskCommand;
import com.everhomes.rest.pmtask.CreateTaskCategoryCommand;
import com.everhomes.rest.pmtask.DeleteCategoryCommand;
import com.everhomes.rest.pmtask.DeleteTaskCategoryCommand;
import com.everhomes.rest.pmtask.EvaluateTaskCommand;
import com.everhomes.rest.pmtask.GetTaskDetailCommand;
import com.everhomes.rest.pmtask.ListCategoriesCommand;
import com.everhomes.rest.pmtask.ListCategoriesResponse;
import com.everhomes.rest.pmtask.ListMyTasksCommand;
import com.everhomes.rest.pmtask.ListMyTasksResponse;
import com.everhomes.rest.pmtask.ListTaskCategoriesCommand;
import com.everhomes.rest.pmtask.ListTaskCategoriesResponse;
import com.everhomes.rest.pmtask.SearchTasksCommand;
import com.everhomes.rest.pmtask.SearchTasksResponse;
import com.everhomes.rest.pmtask.SetTaskStatusCommand;

public interface PmTaskService {
	SearchTasksResponse searchTasks(SearchTasksCommand cmd);
	
	ListMyTasksResponse listMyTasks(ListMyTasksCommand cmd);
	
	void evaluateTask(EvaluateTaskCommand cmd);
	
	void setTaskStatus(SetTaskStatusCommand cmd);
	
	void assignTask(AssignTaskCommand cmd);
	
	CommunityTaskDTO getTaskDetail(GetTaskDetailCommand cmd);
	
	void createNewTask(CreateNewTaskCommand cmd);
	
	void deleteCategory(DeleteCategoryCommand cmd);
	
	void createCategory(CreateCategoryCommand cmd);
	
	ListCategoriesResponse listCategories(ListCategoriesCommand cmd);
	
	void deleteTaskCategory(DeleteTaskCategoryCommand cmd);
	
	void createTaskCategory(CreateTaskCategoryCommand cmd);
	
	ListTaskCategoriesResponse listTaskCategories(ListTaskCategoriesCommand cmd);
	
	void exportTasks(SearchTasksCommand cmd, HttpServletResponse resp);
}
