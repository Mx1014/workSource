package com.everhomes.search;

import java.util.List;

import com.everhomes.equipment.EquipmentInspectionTasks;
import com.everhomes.rest.equipment.ListEquipmentTasksResponse;
import com.everhomes.rest.equipment.SearchEquipmentTasksCommand;

public interface EquipmentTasksSearcher {
	void deleteById(Long id);
    void bulkUpdate(List<EquipmentInspectionTasks> tasks);
    void feedDoc(EquipmentInspectionTasks task);
    void syncFromDb();
    ListEquipmentTasksResponse query(SearchEquipmentTasksCommand cmd); 
}
