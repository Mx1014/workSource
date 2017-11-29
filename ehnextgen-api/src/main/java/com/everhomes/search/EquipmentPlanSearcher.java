package com.everhomes.search;


import com.everhomes.equipment.EquipmentInspectionPlans;
import com.everhomes.rest.equipment.searchEquipmentInspectionPlansCommand;
import com.everhomes.rest.equipment.searchEquipmentInspectionPlansResponse;

import java.util.List;

public interface EquipmentPlanSearcher {
    void deleteById(Long id);
    void bulkUpdate(List<EquipmentInspectionPlans> plans);
    void feedDoc(EquipmentInspectionPlans plan);
    void syncFromDb();
    searchEquipmentInspectionPlansResponse query(searchEquipmentInspectionPlansCommand cmd);

}
