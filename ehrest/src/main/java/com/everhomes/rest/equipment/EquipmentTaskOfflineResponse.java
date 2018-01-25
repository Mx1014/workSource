package com.everhomes.rest.equipment;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.varField.FieldGroupDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <li>offlineTasks:离线任务列表</li>
 * <li>equipments:离线任务列表</li>
 * <li>items:离线任务列表</li>
 * <li>groups:离线类别列表</li>
 */

public class EquipmentTaskOfflineResponse {

    @ItemType(EquipmentTaskOffLineDTO.class)
    private List<EquipmentTaskOffLineDTO> offlineTasks;

    @ItemType(EquipmentStandardRelationDTO.class)
    private List<EquipmentStandardRelationDTO> equipments;

    @ItemType(InspectionItemDTO.class)
    private List<InspectionItemDTO> items;

    @ItemType(FieldGroupDTO.class)
    private List<FieldGroupDTO> groups;

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

    public List<FieldGroupDTO> getGroups() {
        return groups;
    }

    public void setGroups(List<FieldGroupDTO> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
