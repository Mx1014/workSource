package com.everhomes.pmtask;

import java.util.List;

import com.everhomes.rest.pmtask.PmTaskDTO;
import com.everhomes.rest.pmtask.SearchTasksCommand;

public interface PmTaskSearch {
	void deleteById(Long id);
	void bulkUpdate(List<PmTask> tasks);
	void feedDoc(PmTask task);
	void syncPmTask();
	
	List<PmTaskDTO> searchDocsByType(Byte status, String queryString,Long ownerId, String ownerType, Long categoryId, Long startDate, 
			Long endDate, Long addressId, String buildingName,Byte creatorType, Long pageAnchor, Integer pageSize);
	List<PmTaskDTO> searchAllDocsByType(SearchTasksCommand cmd, Integer pageSize);
}
