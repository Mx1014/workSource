package com.everhomes.rest.equipment;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 * <li>taskId: 任务id</li>
 * <li>taskName: 任务名称</li>
 * <li>createTime: 创建时间</li>
 * <li>reviewTime: 审批时间</li>
 * <li>EquipmentStandardRelationOfflineDTO: 离线巡检对象关联DTO 参考{@link com.everhomes.rest.equipment.EquipmentStandardRelationOfflineDTO}</li>
 * </ul>
 */
public class EquipmentTaskOffLineDTO {

    private Long taskId;

    private String name;

    private Timestamp createTime;

    private Timestamp reviewTime;

    @ItemType(EquipmentStandardRelationOfflineDTO.class)
    private List<EquipmentStandardRelationOfflineDTO> equipmentStandardRelations;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        taskId = taskId;
    }

    public List<EquipmentStandardRelationOfflineDTO> getEquipmentStandardRelations() {
        return equipmentStandardRelations;
    }

    public void setEquipmentStandardRelations(List<EquipmentStandardRelationOfflineDTO> equipmentStandardRelations) {
        this.equipmentStandardRelations = equipmentStandardRelations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(Timestamp reviewTime) {
        this.reviewTime = reviewTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
