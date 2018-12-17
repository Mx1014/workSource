package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>entityType: 实体类型, 工作流实体为 flow, 节点实体为 flow_node</li>
 *     <li>entityId: 实体 id</li>
 *     <li>formRelationType: 表单的关联类型 {@link com.everhomes.rest.flow.FlowFormRelationType}</li>
 *     <li>directRelation: 直接关联表单 {@link com.everhomes.rest.flow.FlowFormRelationDataDirectRelation}</li>
 *     <li>customizeField: 全局表单,自定义字段 {@link com.everhomes.rest.flow.FlowFormRelationDataCustomizeField}</li>
 * </ul>
 */
public class UpdateFlowFormCommand {

    @NotNull
    private String entityType;
    @NotNull
    private Long entityId;

    @NotNull
    private Byte formRelationType;
    @NotNull
    private FlowFormRelationDataDirectRelation directRelation;
    @NotNull
    private FlowFormRelationDataCustomizeField customizeField;

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

    public Byte getFormRelationType() {
        return formRelationType;
    }

    public void setFormRelationType(Byte formRelationType) {
        this.formRelationType = formRelationType;
    }

    public FlowFormRelationDataDirectRelation getDirectRelation() {
        return directRelation;
    }

    public void setDirectRelation(FlowFormRelationDataDirectRelation directRelation) {
        this.directRelation = directRelation;
    }

    public FlowFormRelationDataCustomizeField getCustomizeField() {
        return customizeField;
    }

    public void setCustomizeField(FlowFormRelationDataCustomizeField customizeField) {
        this.customizeField = customizeField;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
