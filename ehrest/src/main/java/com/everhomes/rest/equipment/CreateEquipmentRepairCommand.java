package com.everhomes.rest.equipment;

/**
 * Created by rui.jia  2018/1/9 14 :50
 */

public class CreateEquipmentRepairCommand {
    private Long equipmentId;
    private Long taskId;
    //以下为报修参数

    private Integer namespaceId;

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }
}
