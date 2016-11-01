package com.everhomes.search;

import java.util.List;

import com.everhomes.equipment.EquipmentStandardMap;
import com.everhomes.rest.equipment.SearchEquipmentStandardRelationsCommand;
import com.everhomes.rest.equipment.SearchEquipmentStandardRelationsResponse;

public interface EquipmentStandardMapSearcher {
	void deleteById(Long id);
    void bulkUpdate(List<EquipmentStandardMap> maps);
    void feedDoc(EquipmentStandardMap map);
    void syncFromDb();
    SearchEquipmentStandardRelationsResponse query(SearchEquipmentStandardRelationsCommand cmd); 
}
