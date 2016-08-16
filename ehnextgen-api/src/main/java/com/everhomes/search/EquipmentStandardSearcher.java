package com.everhomes.search;

import java.util.List;

import com.everhomes.equipment.EquipmentInspectionStandards;
import com.everhomes.rest.equipment.SearchEquipmentStandardsCommand;
import com.everhomes.rest.equipment.SearchEquipmentStandardsResponse;

public interface EquipmentStandardSearcher {

	void deleteById(Long id);
    void bulkUpdate(List<EquipmentInspectionStandards> standards);
    void feedDoc(EquipmentInspectionStandards standard);
    void syncFromDb();
    SearchEquipmentStandardsResponse query(SearchEquipmentStandardsCommand cmd); 
}
