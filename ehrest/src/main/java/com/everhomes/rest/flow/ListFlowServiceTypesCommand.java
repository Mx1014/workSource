package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>flowUserType: 目前该参数没用 {@link com.everhomes.rest.flow.FlowUserType}</li>
 *     <li>moduleId: 模块id</li>
 *     <li>ownerId: ownerId</li>
 *     <li>ownerType: ownerType</li>
 * </ul>
 */
public class ListFlowServiceTypesCommand {

    private String flowUserType;
    private Long moduleId;
    private String ownerType;
    private Long ownerId;

    public String getFlowUserType() {
        return flowUserType;
    }

    public void setFlowUserType(String flowUserType) {
        this.flowUserType = flowUserType;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
