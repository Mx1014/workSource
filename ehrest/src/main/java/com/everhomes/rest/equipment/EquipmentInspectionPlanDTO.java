package com.everhomes.rest.equipment;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.repeat.RepeatSettingsDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *  <li>id: 计划id</li>
 *  <li>ownerId: 计划所属的主体id</li>
 *  <li>ownerType: 计划所属的主体，参考{@link com.everhomes.rest.quality.OwnerType}</li>
 *  <li>targetId: 计划所属管理处id</li>
 *  <li>targetType: 计划所属管理处类型</li>
 *  <li>targetName: 计划所属管理处名称</li>
 *  <li>planNumber: 计划序号</li>
 *  <li>planType: 计划类别  参考{@link com.everhomes.rest.equipment.StandardType}</li>
 *  <li>name: 计划名称</li>
 *  <li>status: 计划状态 参考{@link com.everhomes.rest.equipment.EquipmentPlanStatus}</li>
 *  <li>reviewResult: 计划审批结果 参考{@link com.everhomes.rest.equipment.EquipmentInspectionPlanResult}</li>
 *  <li>remarks: 计划备注内容</li>
 *  <li>repeatSettings: 执行周期 参考{@link com.everhomes.rest.repeat.RepeatSettingsDTO}</li>
 *  <li>equipmentStandardRelations: 设备标准关系 参考{@link com.everhomes.rest.equipment.EquipmentStandardRelationDTO}</li>
 * </ul>
 */

public class EquipmentInspectionPlanDTO {

    private Long id;

    private Long ownerId;

    private String ownerType;

    private String targetType;

    private Long targetId;

    private String planNumber;

    private Byte planType;

    private String name;

    private Byte status;

    private Byte reviewResult;

    private String remarks;

    private RepeatSettingsDTO repeatSettings;

    @ItemType(EquipmentStandardRelationDTO.class)
    private List<EquipmentStandardRelationDTO> equipmentStandardRelations;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getPlanNumber() {
        return planNumber;
    }

    public void setPlanNumber(String planNumber) {
        this.planNumber = planNumber;
    }

    public Byte getPlanType() {
        return planType;
    }

    public void setPlanType(Byte planType) {
        this.planType = planType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getReviewResult() {
        return reviewResult;
    }

    public void setReviewResult(Byte reviewResult) {
        this.reviewResult = reviewResult;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public RepeatSettingsDTO getRepeatSettings() {
        return repeatSettings;
    }

    public void setRepeatSettings(RepeatSettingsDTO repeatSettings) {
        this.repeatSettings = repeatSettings;
    }

    public List<EquipmentStandardRelationDTO> getEquipmentStandardRelations() {
        return equipmentStandardRelations;
    }

    public void setEquipmentStandardRelations(List<EquipmentStandardRelationDTO> equipmentStandardRelations) {
        this.equipmentStandardRelations = equipmentStandardRelations;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
