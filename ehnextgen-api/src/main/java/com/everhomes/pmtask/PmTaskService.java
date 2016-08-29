package com.everhomes.pmtask;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.everhomes.rest.category.CategoryDTO;
import com.everhomes.rest.pmtask.AssignTaskCommand;
import com.everhomes.rest.pmtask.CancelTaskCommand;
import com.everhomes.rest.pmtask.CloseTaskCommand;
import com.everhomes.rest.pmtask.GetPrivilegesCommand;
import com.everhomes.rest.pmtask.GetPrivilegesDTO;
import com.everhomes.rest.pmtask.GetTaskLogCommand;
import com.everhomes.rest.pmtask.PmTaskDTO;
import com.everhomes.rest.pmtask.CreateTaskCommand;
import com.everhomes.rest.pmtask.CreateTaskCategoryCommand;
import com.everhomes.rest.pmtask.DeleteTaskCategoryCommand;
import com.everhomes.rest.pmtask.EvaluateTaskCommand;
import com.everhomes.rest.pmtask.GetStatisticsCommand;
import com.everhomes.rest.pmtask.GetStatisticsResponse;
import com.everhomes.rest.pmtask.GetTaskDetailCommand;
import com.everhomes.rest.pmtask.ListUserTasksCommand;
import com.everhomes.rest.pmtask.ListUserTasksResponse;
import com.everhomes.rest.pmtask.ListTaskCategoriesCommand;
import com.everhomes.rest.pmtask.ListTaskCategoriesResponse;
import com.everhomes.rest.pmtask.PmTaskLogDTO;
import com.everhomes.rest.pmtask.SearchTaskStatisticsCommand;
import com.everhomes.rest.pmtask.SearchTaskStatisticsResponse;
import com.everhomes.rest.pmtask.SearchTasksCommand;
import com.everhomes.rest.pmtask.SearchTasksResponse;
import com.everhomes.rest.pmtask.CompleteTaskCommand;

public interface PmTaskService {
	SearchTasksResponse searchTasks(SearchTasksCommand cmd);
	
	ListUserTasksResponse listUserTasks(ListUserTasksCommand cmd);
	
	void evaluateTask(EvaluateTaskCommand cmd);
	
	void completeTask(CompleteTaskCommand cmd);
	
	void closeTask(CloseTaskCommand cmd);
	
	void assignTask(AssignTaskCommand cmd);
	
	PmTaskDTO getTaskDetail(GetTaskDetailCommand cmd);
	
	PmTaskDTO createTask(CreateTaskCommand cmd);
	
	PmTaskDTO createTaskByAdmin(CreateTaskCommand cmd);
	
	void deleteTaskCategory(DeleteTaskCategoryCommand cmd);
	
	CategoryDTO createTaskCategory(CreateTaskCategoryCommand cmd);
	
	ListTaskCategoriesResponse listTaskCategories(ListTaskCategoriesCommand cmd);
	
	void exportTasks(SearchTasksCommand cmd, HttpServletResponse resp);
	
	SearchTaskStatisticsResponse searchTaskStatistics(SearchTaskStatisticsCommand cmd);
	
	GetStatisticsResponse getStatistics(GetStatisticsCommand cmd);
	
	PmTaskLogDTO getTaskLog(GetTaskLogCommand cmd);
	
	void cancelTask(CancelTaskCommand cmd);
	
	GetPrivilegesDTO getPrivileges(GetPrivilegesCommand cmd);
	
	void exportStatistics(GetStatisticsCommand cmd, HttpServletResponse resp);
	
	void createStatistics();
}
