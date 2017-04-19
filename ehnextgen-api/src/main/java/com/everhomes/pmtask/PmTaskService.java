package com.everhomes.pmtask;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.everhomes.rest.category.CategoryDTO;
import com.everhomes.rest.pmtask.*;

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

	void deleteTaskHistoryAddress(DeleteTaskHistoryAddressCommand cmd);

	PmTaskHistoryAddressDTO createTaskHistoryAddress(CreateTaskHistoryAddressCommand cmd);
}
