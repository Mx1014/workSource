package com.everhomes.rest.equipment;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * Created by rui.jia  2018/1/9 09 :41
 */

public class EquipmentTaskOfflineResponse {

    @ItemType(EquipmentTaskOffLineDTO.class)
    private List<EquipmentTaskOffLineDTO> offlineTasks;

    @ItemType(EquipmentStandardRelationDTO.class)
    private List<EquipmentStandardRelationDTO> equipments;

    @ItemType(InspectionItemDTO.class)
    private List<InspectionItemDTO> items;

    public List<EquipmentTaskOffLineDTO> getOfflineTasks() {
        return offlineTasks;
    }

    public void setOfflineTasks(List<EquipmentTaskOffLineDTO> offlineTasks) {
        this.offlineTasks = offlineTasks;
    }

    public List<EquipmentStandardRelationDTO> getEquipments() {
        return equipments;
    }

    public void setEquipments(List<EquipmentStandardRelationDTO> equipments) {
        this.equipments = equipments;
    }

    public List<InspectionItemDTO> getItems() {
        return items;
    }

    public void setItems(List<InspectionItemDTO> items) {
        this.items = items;
    }
}
