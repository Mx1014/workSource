package com.everhomes.pmtask;

import java.util.List;

import com.everhomes.rest.pmtask.PmTaskDTO;

public interface PmTaskSearch {
	void deleteById(Long id);
	void bulkUpdate(List<PmTask> tasks);
	void feedDoc(PmTask task);
	void syncPmTask();
	
	List<PmTaskDTO> searchDocsByType(Byte status, String queryString,Long ownerId, String ownerType, Long categoryId, Long startDate, 
			Long endDate, Long addressId, String buildingName, Long pageAnchor, Integer pageSize);
}
