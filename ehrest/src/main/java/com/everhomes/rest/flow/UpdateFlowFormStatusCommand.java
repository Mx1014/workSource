package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>entityType: entityType</li>
 *     <li>entityId: entityId</li>
 *     <li>status: status</li>
 * </ul>
 */
public class UpdateFlowFormStatusCommand {

    @NotNull
    private String entityType;
    @NotNull
    private Long entityId;
    @NotNull
    private Byte status;

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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
