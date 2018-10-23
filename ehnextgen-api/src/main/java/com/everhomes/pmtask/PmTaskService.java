package com.everhomes.pmtask;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.everhomes.pay.order.OrderPaymentNotificationCommand;
import com.everhomes.rest.address.ListApartmentByBuildingNameCommand;
import com.everhomes.rest.address.ListApartmentByBuildingNameCommandResponse;
import com.everhomes.rest.asset.ListPayeeAccountsCommand;
import com.everhomes.rest.category.CategoryDTO;
import com.everhomes.rest.community.ListBuildingCommand;
import com.everhomes.rest.community.ListBuildingCommandResponse;
import com.everhomes.rest.order.ListBizPayeeAccountDTO;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.pmtask.*;

public interface PmTaskService {
	SearchTasksResponse searchTasks(SearchTasksCommand cmd);
	SearchTasksResponse searchTasksWithoutAuth(SearchTasksCommand cmd);

	ListUserTasksResponse listUserTasks(ListUserTasksCommand cmd);
	
//	void evaluateTask(EvaluateTaskCommand cmd);
	
//	void completeTask(CompleteTaskCommand cmd);
	
//	void closeTask(CloseTaskCommand cmd);

//	void assignTask(AssignTaskCommand cmd);

	ListApartmentByBuildingNameCommandResponse listApartmentsByBuildingName(ListApartmentByBuildingNameCommand cmd);

	PmTaskDTO getTaskDetail(GetTaskDetailCommand cmd);
	
	PmTaskDTO createTask(CreateTaskCommand cmd);

	ListBuildingCommandResponse listBuildings(ListBuildingCommand cmd);
	
	PmTaskDTO createTaskByOrg(CreateTaskCommand cmd);
	
	void deleteTaskCategory(DeleteTaskCategoryCommand cmd);
	
	CategoryDTO createTaskCategory(CreateTaskCategoryCommand cmd);
	
	ListTaskCategoriesResponse listTaskCategories(ListTaskCategoriesCommand cmd);
	
	List<CategoryDTO> listAllTaskCategories(ListAllTaskCategoriesCommand cmd);
	
	void exportTasks(SearchTasksCommand cmd, HttpServletResponse resp, HttpServletRequest req);
	
	SearchTaskStatisticsResponse searchTaskStatistics(SearchTaskStatisticsCommand cmd);
	
	GetStatisticsResponse getStatistics(GetStatisticsCommand cmd);
	
//	PmTaskLogDTO getTaskLog(GetTaskLogCommand cmd);
	
//	void cancelTask(CancelTaskCommand cmd);
	
//	GetPrivilegesDTO getPrivileges(GetPrivilegesCommand cmd);
	
	void exportStatistics(GetStatisticsCommand cmd, HttpServletResponse resp);
	
	void exportListStatistics(SearchTaskStatisticsCommand cmd, HttpServletResponse resp);
	
//	ListOperatePersonnelsResponse listOperatePersonnels(ListOperatePersonnelsCommand cmd);
	
//	void revisit(RevisitCommand cmd);
	
	SearchTaskCategoryStatisticsResponse searchTaskCategoryStatistics(SearchTaskStatisticsCommand cmd);
	
	void exportTaskCategoryStatistics(SearchTaskStatisticsCommand cmd, HttpServletResponse resp);
	
	TaskCategoryStatisticsDTO getTaskCategoryStatistics(SearchTaskStatisticsCommand cmd);
	
	void updateTaskByOrg(UpdateTaskCommand cmd);
	
	ListAuthorizationCommunityByUserResponse listAuthorizationCommunityByUser(ListAuthorizationCommunityCommand cmd);
	
	GetUserRelatedAddressByCommunityResponse getUserRelatedAddressesByCommunity(GetUserRelatedAddressesByCommunityCommand cmd);

	NamespaceHandlerDTO getNamespaceHandler(GetNamespaceHandlerCommand cmd);

	GetIfHideRepresentResponse getIfHideRepresent(GetIfHideRepresentCommand cmd);

//	void synchronizedData(SearchTasksCommand cmd);

	void deleteTaskHistoryAddress(DeleteTaskHistoryAddressCommand cmd);

	PmTaskHistoryAddressDTO createTaskHistoryAddress(CreateTaskHistoryAddressCommand cmd);

	void notifyTaskResult(NotifyTaskResultCommand cmd);

	void exportTasksCard(ExportTasksCardCommand cmd, HttpServletResponse response);

	void changeTasksStatus(UpdateTasksStatusCommand cmd);

	ListAuthorizationCommunityByUserResponse listOrganizationCommunityByUser(ListOrganizationCommunityByUserCommand cmd);

	ListOrganizationCommunityBySceneTokenResponse listOrganizationCommunityBySceneToken(ListOrganizationCommunityBySceneTokenCommand cmd);

	void syncTaskStatistics(HttpServletResponse resp);

	void syncCategories();

	PmTaskStatDTO getStatSurvey(GetTaskStatCommand cmd);

	List<PmTaskStatSubDTO> getStatByCategory(GetTaskStatCommand cmd);

	List<PmTaskStatDTO> getStatByCreator(GetTaskStatCommand cmd);

	List<PmTaskStatDTO> getStatByStatus(GetTaskStatCommand cmd);

	List<PmTaskStatSubDTO> getStatByArea(GetTaskStatCommand cmd);

	void exportTaskStat(GetTaskStatCommand cmd, HttpServletResponse resp);

	Object getThirdAddress(HttpServletRequest req);

	Object createThirdTask(HttpServletRequest req);

	Object listThirdTasks(HttpServletRequest req);

	Object getThirdTaskDetail(HttpServletRequest req);

	Object getThirdCategories(HttpServletRequest req);

	Object getThirdProjects(HttpServletRequest req);

	Object createThirdEvaluation(HttpServletRequest req);

	Object getThirdEvaluation(HttpServletRequest req);

	Object submitThirdAttachment(HttpServletRequest req);

    List<SearchTasksByOrgDTO> listTasksByOrg(SearchTasksByOrgCommand cmd17);

    List<SearchTasksByOrgDTO> searchOrgTasks(SearchOrgTasksCommand cmd);
	List<PmTaskEvalStatDTO> getEvalStat(GetEvalStatCommand cmd);

//	----------------------------------------- 3.7 -----------------------------------------
	PmTaskConfigDTO setPmTaskConfig(SetPmTaskConfigCommand cmd);

	PmTaskConfigDTO searchPmTaskConfigByProject(GetPmTaskConfigCommand cmd);

	void createOrderDetails(CreateOrderDetailsCommand cmd);

	void modifyOrderDetails(CreateOrderDetailsCommand cmd);

	PmTaskOrderDTO searchOrderDetailsByTaskId(GetOrderDetailsCommand cmd);

	void syncOrderDetails();

	void clearOrderDetails();

	List<ListBizPayeeAccountDTO> listPayeeAccounts(ListPayeeAccountsCommand cmd);

	PreOrderDTO payBills(CreatePmTaskOrderCommand cmd);

	void payNotify(OrderPaymentNotificationCommand cmd);

//	List<PayOrderDTO> listBills(ListBillsCommand cmd);

}
