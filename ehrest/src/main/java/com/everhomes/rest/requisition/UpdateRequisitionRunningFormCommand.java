package com.everhomes.rest.requisition;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId : namespaceId</li>
 *     <li>sourcId: 需要更新启用表单的审批项id</li>
 *     <li>formOriginId: 启用的表单formOriginId</li>
 *     <li>formVersion: 启用的表单的formVersion</li>
 * </ul>
 */
public class UpdateRequisitionRunningFormCommand {
    private Integer namespaceId;
    private Long sourceId;
    private Long formOriginId;
    private Long formVersion;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public Long getFormOriginId() {
        return formOriginId;
    }

    public void setFormOriginId(Long formOriginId) {
        this.formOriginId = formOriginId;
    }

    public Long getFormVersion() {
        return formVersion;
    }

    public void setFormVersion(Long formVersion) {
        this.formVersion = formVersion;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
