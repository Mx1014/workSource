package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>entityType: 实体类型, 工作流实体为 flow, 节点实体为 flow_node</li>
 *     <li>entityId: 实体 id</li>
 *     <li>formOriginId: formOriginId</li>
 *     <li>formVersion: formVersion</li>
 * </ul>
 */
public class UpdateFlowFormCommand {

    private String entityType;
    private Long entityId;
    private Long formOriginId;
    private Long formVersion;

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
