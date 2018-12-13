package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>entityType: 实体类型, 工作流: flow, flow_node</li>
 *     <li>entityId: 实体 id</li>
 *     <li>status: 开关状态， 0：关，1：开</li>
 *     <li>formRelationType: 表单的关联类型 {@link FlowFormRelationType}</li>
 *     <li>directRelation: 直接关联表单 {@link FlowFormRelationDataDirectRelationDTO}</li>
 *     <li>customizeField: 全局表单，自定义字段 {@link FlowFormRelationDataCustomizeFieldDTO}</li>
 * </ul>
 */
public class FlowFormRelationDTO {

    private String entityType;
    private Long entityId;

    private Byte status;
    private Byte formRelationType;
    private FlowFormRelationDataDirectRelationDTO directRelation;
    private FlowFormRelationDataCustomizeFieldDTO customizeField;

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getFormRelationType() {
        return formRelationType;
    }

    public void setFormRelationType(Byte formRelationType) {
        this.formRelationType = formRelationType;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public FlowFormRelationDataDirectRelationDTO getDirectRelation() {
        return directRelation;
    }

    public void setDirectRelation(FlowFormRelationDataDirectRelationDTO directRelation) {
        this.directRelation = directRelation;
    }

    public FlowFormRelationDataCustomizeFieldDTO getCustomizeField() {
        return customizeField;
    }

    public void setCustomizeField(FlowFormRelationDataCustomizeFieldDTO customizeField) {
        this.customizeField = customizeField;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

