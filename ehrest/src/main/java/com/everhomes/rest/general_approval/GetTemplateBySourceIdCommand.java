package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间id</li>
 * <li>sourceType: 源类型</li>
 * <li>sourceId: 源Id</li>
 * <li>valuesOfFlowCaseId: 工作流ID，有值时获取对应表单的值(复制审批单功能)</li>
 * </ul>
 */
public class GetTemplateBySourceIdCommand {
    private Integer namespaceId;
    private Long ownerId;
    private String ownerType;
    private String sourceType;
    private Long sourceId;
    private Long valuesOfFlowCaseId;

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
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

    public Long getValuesOfFlowCaseId() {
        return valuesOfFlowCaseId;
    }

    public void setValuesOfFlowCaseId(Long valuesOfFlowCaseId) {
        this.valuesOfFlowCaseId = valuesOfFlowCaseId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
