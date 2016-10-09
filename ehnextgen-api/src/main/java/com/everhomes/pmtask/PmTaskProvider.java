package com.everhomes.pmtask;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.namespace.Namespace;

public interface PmTaskProvider {
	void createTask(PmTask pmTask);
	
	PmTask findTaskById(Long id);
	
	List<PmTaskLog> listPmTaskLogs(Long taskId, Byte status);
	
	List<PmTaskAttachment> listPmTaskAttachments(Long ownerId, String contentType);
	
	void createTaskAttachment(PmTaskAttachment pmTaskAttachment);
	
	void updateTask(PmTask pmTask);
	
	void createTaskLog(PmTaskLog pmTaskLog);
	
	List<PmTask> listPmTask(String ownerType, Long ownerId, Long userId, Byte status,
			Long pageAnchor, Integer pageSize);
	
	List<PmTask> listPmTask(String ownerType, Long ownerId, Long userId, Long pageAnchor, Integer pageSize);
	
	PmTaskLog findTaskLogById(Long id);
	
	List<Namespace> listNamespace();
	
	Integer countTask(Long ownerId, Byte status, Long categoryId, Byte star, Timestamp startDate, Timestamp endDate);
	
	void createTaskStatistics(PmTaskStatistics statistics);
	
	List<PmTaskStatistics> searchTaskStatistics(Integer namespaceId, Long ownerId, Long categoryId, String keyword, Timestamp dateStr,
			Long pageAnchor, Integer pageSize);
	
	Integer countTaskStatistics(Long ownerId, Long categoryId, Timestamp dateStr);
}
