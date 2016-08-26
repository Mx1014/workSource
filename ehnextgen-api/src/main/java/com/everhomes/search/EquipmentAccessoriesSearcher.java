package com.everhomes.search;

import java.util.List;

import com.everhomes.equipment.EquipmentInspectionAccessories;
import com.everhomes.rest.equipment.SearchEquipmentAccessoriesCommand;
import com.everhomes.rest.equipment.SearchEquipmentAccessoriesResponse;

public interface EquipmentAccessoriesSearcher {
	void deleteById(Long id);
    void bulkUpdate(List<EquipmentInspectionAccessories> accessories);
    void feedDoc(EquipmentInspectionAccessories accessory);
    void syncFromDb();
    SearchEquipmentAccessoriesResponse query(SearchEquipmentAccessoriesCommand cmd); 

}
