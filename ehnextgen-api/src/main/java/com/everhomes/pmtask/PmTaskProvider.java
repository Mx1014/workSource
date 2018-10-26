package com.everhomes.pmtask;

import com.everhomes.namespace.Namespace;

import java.sql.Timestamp;
import java.util.List;

public interface PmTaskProvider {

	void deleteTask(PmTask pmTask);
	
	void createTask(PmTask pmTask);
	
	PmTask findTaskById(Long id);
	
	List<PmTaskLog> listPmTaskLogs(Long taskId, Byte status);
	
	List<PmTaskAttachment> listPmTaskAttachments(Long ownerId, String contentType);
	
	void createTaskAttachment(PmTaskAttachment pmTaskAttachment);
	
	void updateTask(PmTask pmTask);
	
	void createTaskLog(PmTaskLog pmTaskLog);
	
//	List<PmTask> listPmTask(String ownerType, Long ownerId, Long userId, Byte status, Long taskCategoryId,
//			Long pageAnchor, Integer pageSize);
	
	List<PmTask> listPmTask(String ownerType, Long ownerId, Long userId, Long pageAnchor, Integer pageSize);
	
	PmTaskLog findTaskLogById(Long id);
	
	List<Namespace> listNamespace();
	
	Integer countTask(Long ownerId, Byte status, Long taskCategoryId, Long categoryId, String star, Timestamp startDate, Timestamp endDate);
	
	void createTaskStatistics(PmTaskStatistics statistics);
	
	List<PmTaskStatistics> searchTaskStatistics(Integer namespaceId, Long ownerId, Long taskCategoryId, String keyword, Timestamp dateStr,
			Long pageAnchor, Integer pageSize);
	
	Integer countTaskStatistics(Long ownerId, Long taskCategoryId, Timestamp dateStr);
	
//	List<PmTaskTarget> listTaskTargets(String ownerType, Long ownerId, Byte roleId, Long pageAnchor, Integer pageSize);

	List<PmTaskLog> listRepairByUpdateTimeAndAnchor(Integer namespaceId, Long timestamp, Long pageAnchor, int pageSize);

	List<PmTaskLog> listRepairByUpdateTime(Integer namespaceId, Long timestamp, int pageSize);

	void createTaskHistoryAddress(PmTaskHistoryAddress pmTaskHistoryAddress);

	List<PmTaskHistoryAddress> listTaskHistoryAddresses(Integer namespaceId, String ownerType, Long ownerId, Long userId,
														Long pageAnchor, Integer pageSize);

	void updateTaskHistoryAddress(PmTaskHistoryAddress pmTaskHistoryAddress);

	PmTaskHistoryAddress findTaskHistoryAddressById(Long id);

	List<PmTask> listTasksById(List<Long> ids);

	List<PmTask> findTaskByOrderId(String orderId);

	List<PmTask> listTaskByStat(Integer namespaceId, List<Long> ownerIds, Timestamp dateStart, Timestamp dateEnd, List<Long> taskcategoryIds);

    List<PmTask> listPmTasksByOrgId(Integer namespaceId, Long communityId, Long organizationId);

    List<PmTask> findTasksByOrg(Long communityId, Integer namespaceId, Long organizationId, Long taskCategoryId);

	PmTask findTaskByFlowCaseId(Long flowCaseId);

	PmTaskConfig createPmTaskConfig(PmTaskConfig bean);
	PmTaskConfig updatePmTaskConfig(PmTaskConfig bean);
	PmTaskConfig findPmTaskConfigbyOwnerId(Integer namespaceId, String ownerType, Long ownerId, Long taskCategoryId);

//	订单明细CRUD
	void createOrderDetails(List<PmTaskOrderDetail> beans);
	List<PmTaskOrderDetail> findOrderDetailsByTaskId(Integer namespaceId, String ownerType, Long ownerId, Long taskId);
	void deleteOrderDetailsByOrderId(Long orderId);

//	订单CRUD
	PmTaskOrder createPmTaskOrder(PmTaskOrder bean);

	PmTaskOrder updatePmTaskOrder(PmTaskOrder bean);

	PmTaskOrder findPmTaskOrderById(Long id);

	PmTaskOrder findPmTaskOrderByTaskId(Long taskId);

	PmTaskOrder findPmTaskOrderByBizOrderNum(String BizOrderNum);

	void clearOrderDetails();

	void deletePmTaskOrder(Long id);

//	国贸对接用户映射表
	void createArchibusUser(PmTaskArchibusUserMapping bean);
	void updateArchibusUser(PmTaskArchibusUserMapping bean);
	void deleteArchibusUser(Long id);
	PmTaskArchibusUserMapping findArchibusUserbyPhone(String phoneNum);
	PmTaskArchibusUserMapping findArchibusUserbyArchibusId(String archibusUid);

//	服务类型
	Long createCategory(PmTaskCategory bean);
	void updateCategory(PmTaskCategory bean);
	PmTaskCategory findCategoryById(Long id);
	List<PmTaskCategory> listTaskCategories(Integer namespaceId, String ownerType, Long ownerId, Long appId,Long parentId,
										String keyword, Long pageAnchor, Integer pageSize);
	PmTaskCategory findCategoryByNamespaceAndName(Long parentId, Integer namespaceId,String ownerType,
											Long ownerId,Long appId,String categoryName);

}
