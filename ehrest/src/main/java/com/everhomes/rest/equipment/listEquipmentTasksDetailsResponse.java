package com.everhomes.rest.equipment;


import com.everhomes.util.StringHelper;

import java.util.List;

public class listEquipmentTasksDetailsResponse {
    private List<EquipmentTaskDTO> tasks;

    private List<EquipmentStandardRelationDTO> EquipmentStandardRelations;

    private List<InspectionItemDTO> inspectionItems;

    public List<EquipmentTaskDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<EquipmentTaskDTO> tasks) {
        this.tasks = tasks;
    }

    public List<EquipmentStandardRelationDTO> getEquipmentStandardRelations() {
        return EquipmentStandardRelations;
    }

    public void setEquipmentStandardRelations(List<EquipmentStandardRelationDTO> equipmentStandardRelations) {
        EquipmentStandardRelations = equipmentStandardRelations;
    }

    public List<InspectionItemDTO> getInspectionItems() {
        return inspectionItems;
    }

    public void setInspectionItems(List<InspectionItemDTO> inspectionItems) {
        this.inspectionItems = inspectionItems;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
