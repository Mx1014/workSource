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
	
	List<PmTask> listPmTask(String ownerType, Long ownerId, Long categoryId, Byte status, Byte unstatus, String keyword, 
			Long pageAnchor, Integer pageSize);
	
	PmTaskLog findTaskLogById(Long id);
}
