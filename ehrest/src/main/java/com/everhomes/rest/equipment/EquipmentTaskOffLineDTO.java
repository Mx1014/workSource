package com.everhomes.rest.equipment;


import com.everhomes.util.StringHelper;

import java.util.List;
/**
 * <ul>
 *  <li>taskId: 任务id</li>
 *  <li>EquipmentStandardRelationOfflineDTO: 离线巡检对象关联DTO 参考{@link com.everhomes.rest.equipment.EquipmentStandardRelationOfflineDTO}</li>
 * </ul>
 */
public class EquipmentTaskOffLineDTO {
    private  Long   TaskId;
    private List<EquipmentStandardRelationOfflineDTO> equipmentStandardRelations;

    public Long getTaskId() {
        return TaskId;
    }

    public void setTaskId(Long taskId) {
        TaskId = taskId;
    }

    public List<EquipmentStandardRelationOfflineDTO> getEquipmentStandardRelations() {
        return equipmentStandardRelations;
    }

    public void setEquipmentStandardRelations(List<EquipmentStandardRelationOfflineDTO> equipmentStandardRelations) {
        this.equipmentStandardRelations = equipmentStandardRelations;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
