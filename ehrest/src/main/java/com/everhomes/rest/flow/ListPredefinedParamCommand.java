// @formatter:off
package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>flowId: flowId</li>
 *     <li>ownerType: ownerType</li>
 *     <li>ownerId: ownerId</li>
 *     <li>entityType: entityType {@link com.everhomes.rest.flow.FlowEntityType}</li>
 * </ul>
 */
public class ListPredefinedParamCommand {

    private Long flowId;
    private String entityType;
    private String ownerType;
    private Long ownerId;

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
