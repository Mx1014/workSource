package com.everhomes.pmtask;

import java.util.List;

public interface PmTaskProvider {
	void createTask(PmTask pmTask);
	
	PmTask findTaskById(Long id);
	
	List<PmTaskLog> listPmTaskLogs(Long taskId);
	
	List<PmTaskAttachment> listPmTaskAttachments(Long ownerId);
	
	void createTaskAttachment(PmTaskAttachment pmTaskAttachment);
	
	void updateTask(PmTask pmTask);
	
	void createTaskLog(PmTaskLog pmTaskLog);
	
	List<PmTask> listPmTask(String ownerType, Long ownerId, Long userId, Byte status, Boolean flag,
			Long pageAnchor, Integer pageSize);
	
	PmTaskLog findTaskLogById(Long id);
}
