package com.everhomes.pmtask;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.namespace.Namespace;

public interface PmTaskProvider {
	
	void createTaskTarget(PmTaskTarget pmTaskTarget);

	void deleteTask(PmTask pmTask);
	
	void updateTaskTarget(PmTaskTarget pmTaskTarget);

	void deleteTaskTarget(PmTaskTarget pmTaskTarget);
	
	void createTask(PmTask pmTask);
	
	PmTask findTaskById(Long id);
	
	List<PmTaskLog> listPmTaskLogs(Long taskId, Byte status);
	
	List<PmTaskAttachment> listPmTaskAttachments(Long ownerId, String contentType);
	
	void createTaskAttachment(PmTaskAttachment pmTaskAttachment);
	
	void updateTask(PmTask pmTask);
	
	void createTaskLog(PmTaskLog pmTaskLog);
	
	List<PmTask> listPmTask(String ownerType, Long ownerId, Long userId, Byte status, Long taskCategoryId,
			Long pageAnchor, Integer pageSize);
	
	List<PmTask> listPmTask(String ownerType, Long ownerId, Long userId, Long pageAnchor, Integer pageSize);
	
	PmTaskLog findTaskLogById(Long id);
	
	List<Namespace> listNamespace();
	
	Integer countTask(Long ownerId, Byte status, Long taskCategoryId, Long categoryId, Byte star, Timestamp startDate, Timestamp endDate);
	
	void createTaskStatistics(PmTaskStatistics statistics);
	
	List<PmTaskStatistics> searchTaskStatistics(Integer namespaceId, Long ownerId, Long taskCategoryId, String keyword, Timestamp dateStr,
			Long pageAnchor, Integer pageSize);
	
	Integer countTaskStatistics(Long ownerId, Long taskCategoryId, Timestamp dateStr);
	
	List<PmTaskTarget> listTaskTargets(String ownerType, Long ownerId, Byte roleId, Long pageAnchor, Integer pageSize);
	
	PmTaskTarget findTaskTarget(String ownerType, Long ownerId, Byte roleId, String targetType, Long targetId);
	
	Integer countUserProccsingPmTask(String ownerType, Long ownerId, Long userId);
	
	List<PmTask> listPmTask(String ownerType, Long ownerId, Long taskCategoryId, Byte status);
	
	List<PmTask> listPmTask4Stat(String ownerType, Long ownerId, Long taskCategoryId, Long userId, Timestamp startDate, Timestamp endDate);
	
	void createTaskTargetStatistic(PmTaskTargetStatistic pmTaskTargetStatistic);
	
	List<PmTaskTargetStatistic> searchTaskTargetStatistics(Integer namespaceId, Long ownerId, Long taskCategoryId, Long userId, Timestamp dateStr,
			Long pageAnchor, Integer pageSize);

	List<PmTaskLog> listRepairByUpdateTimeAndAnchor(Integer namespaceId, Long timestamp, Long pageAnchor, int pageSize);

	List<PmTaskLog> listRepairByUpdateTime(Integer namespaceId, Long timestamp, int pageSize);

	void createTaskHistoryAddress(PmTaskHistoryAddress pmTaskHistoryAddress);

	List<PmTaskHistoryAddress> listTaskHistoryAddresses(Integer namespaceId, String ownerType, Long ownerId, Long userId,
														Long pageAnchor, Integer pageSize);

	void updateTaskHistoryAddress(PmTaskHistoryAddress pmTaskHistoryAddress);

	PmTaskHistoryAddress findTaskHistoryAddressById(Long id);

	List<PmTask> listTasksById(List<Long> ids);

	public List<PmTask> findTaskByOrderId(String orderId);
}
