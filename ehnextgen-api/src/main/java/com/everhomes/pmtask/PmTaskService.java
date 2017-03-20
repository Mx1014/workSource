package com.everhomes.pmtask;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.everhomes.rest.category.CategoryDTO;
import com.everhomes.rest.pmtask.AssignTaskCommand;
import com.everhomes.rest.pmtask.CancelTaskCommand;
import com.everhomes.rest.pmtask.CloseTaskCommand;
import com.everhomes.rest.pmtask.CreateTaskOperatePersonCommand;
import com.everhomes.rest.pmtask.DeleteTaskOperatePersonCommand;
import com.everhomes.rest.pmtask.GetNamespaceHandlerCommand;
import com.everhomes.rest.pmtask.GetPrivilegesCommand;
import com.everhomes.rest.pmtask.GetPrivilegesDTO;
import com.everhomes.rest.pmtask.GetTaskLogCommand;
import com.everhomes.rest.pmtask.GetUserRelatedAddressByCommunityResponse;
import com.everhomes.rest.pmtask.GetUserRelatedAddressesByCommunityCommand;
import com.everhomes.rest.pmtask.ListAllTaskCategoriesCommand;
import com.everhomes.rest.pmtask.ListAuthorizationCommunityByUserResponse;
import com.everhomes.rest.pmtask.ListAuthorizationCommunityCommand;
import com.everhomes.rest.pmtask.ListOperatePersonnelsCommand;
import com.everhomes.rest.pmtask.ListOperatePersonnelsResponse;
import com.everhomes.rest.pmtask.NamespaceHandlerDTO;
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
import com.everhomes.rest.pmtask.RevisitCommand;
import com.everhomes.rest.pmtask.SearchTaskCategoryStatisticsResponse;
import com.everhomes.rest.pmtask.SearchTaskOperatorStatisticsCommand;
import com.everhomes.rest.pmtask.SearchTaskOperatorStatisticsResponse;
import com.everhomes.rest.pmtask.SearchTaskStatisticsCommand;
import com.everhomes.rest.pmtask.SearchTaskStatisticsResponse;
import com.everhomes.rest.pmtask.SearchTasksCommand;
import com.everhomes.rest.pmtask.SearchTasksResponse;
import com.everhomes.rest.pmtask.CompleteTaskCommand;
import com.everhomes.rest.pmtask.TaskCategoryStatisticsDTO;
import com.everhomes.rest.pmtask.UpdateTaskCommand;

public interface PmTaskService {
	SearchTasksResponse searchTasks(SearchTasksCommand cmd);
	
	ListUserTasksResponse listUserTasks(ListUserTasksCommand cmd);
	
	void evaluateTask(EvaluateTaskCommand cmd);
	
	void completeTask(CompleteTaskCommand cmd);
	
	void closeTask(CloseTaskCommand cmd);

	void assignTask(AssignTaskCommand cmd);
	
	PmTaskDTO getTaskDetail(GetTaskDetailCommand cmd);
	
	PmTaskDTO createTask(CreateTaskCommand cmd);
	
	PmTaskDTO createTaskByOrg(CreateTaskCommand cmd);
	
	void deleteTaskCategory(DeleteTaskCategoryCommand cmd);
	
	CategoryDTO createTaskCategory(CreateTaskCategoryCommand cmd);
	
	ListTaskCategoriesResponse listTaskCategories(ListTaskCategoriesCommand cmd);
	
	List<CategoryDTO> listAllTaskCategories(ListAllTaskCategoriesCommand cmd);
	
	void exportTasks(SearchTasksCommand cmd, HttpServletResponse resp, HttpServletRequest req);
	
	SearchTaskStatisticsResponse searchTaskStatistics(SearchTaskStatisticsCommand cmd);
	
	GetStatisticsResponse getStatistics(GetStatisticsCommand cmd);
	
	PmTaskLogDTO getTaskLog(GetTaskLogCommand cmd);
	
	void cancelTask(CancelTaskCommand cmd);
	
	GetPrivilegesDTO getPrivileges(GetPrivilegesCommand cmd);
	
	void exportStatistics(GetStatisticsCommand cmd, HttpServletResponse resp);
	
	void exportListStatistics(SearchTaskStatisticsCommand cmd, HttpServletResponse resp);
	
	void createStatistics();
	
	ListOperatePersonnelsResponse listOperatePersonnels(ListOperatePersonnelsCommand cmd);
	
	void revisit(RevisitCommand cmd);
	
	void createTaskOperatePerson(CreateTaskOperatePersonCommand cmd);
	
	void deleteTaskOperatePerson(DeleteTaskOperatePersonCommand cmd);
	
	SearchTaskCategoryStatisticsResponse searchTaskCategoryStatistics(SearchTaskStatisticsCommand cmd);
	
	void exportTaskCategoryStatistics(SearchTaskStatisticsCommand cmd, HttpServletResponse resp);
	
	TaskCategoryStatisticsDTO getTaskCategoryStatistics(SearchTaskStatisticsCommand cmd);
	
	void updateTaskByOrg(UpdateTaskCommand cmd);
	
	ListAuthorizationCommunityByUserResponse listAuthorizationCommunityByUser(ListAuthorizationCommunityCommand cmd);
	
	GetUserRelatedAddressByCommunityResponse getUserRelatedAddressesByCommunity(GetUserRelatedAddressesByCommunityCommand cmd);
	
	SearchTaskOperatorStatisticsResponse searchTaskOperatorStatistics(SearchTaskOperatorStatisticsCommand cmd);
	
	void exportTaskOperatorStatistics(SearchTaskOperatorStatisticsCommand cmd, HttpServletResponse resp);
	
	void createTaskTargetStatistics();
	
	NamespaceHandlerDTO getNamespaceHandler(GetNamespaceHandlerCommand cmd);

	void synchronizedData(SearchTasksCommand cmd);
}
