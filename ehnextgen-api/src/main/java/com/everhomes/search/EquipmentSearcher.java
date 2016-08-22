package com.everhomes.search;

import java.util.List;

import com.everhomes.equipment.EquipmentInspectionEquipments;
import com.everhomes.rest.equipment.SearchEquipmentStandardRelationsCommand;
import com.everhomes.rest.equipment.SearchEquipmentStandardRelationsResponse;
import com.everhomes.rest.equipment.SearchEquipmentsCommand;
import com.everhomes.rest.equipment.SearchEquipmentsResponse;

public interface EquipmentSearcher {

	void deleteById(Long id);
    void bulkUpdate(List<EquipmentInspectionEquipments> equipments);
    void feedDoc(EquipmentInspectionEquipments equipment);
    void syncFromDb();
    SearchEquipmentsResponse queryEquipments(SearchEquipmentsCommand cmd); 
    SearchEquipmentStandardRelationsResponse queryEquipmentStandardRelations(SearchEquipmentStandardRelationsCommand cmd);
}
