package com.everhomes.rest.equipment;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <li>offlineTasks:离线任务列表</li>
 * <li>equipments:离线任务列表</li>
 * <li>items:离线任务列表</li>
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
