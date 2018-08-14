package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 *     <li>flowIds: flowIds</li>
 *     <li>projectType: projectType</li>
 *     <li>projectId: projectId</li>
 *     <li>ownerType: ownerType</li>
 *     <li>ownerId: ownerId</li>
 * </ul>
 */
public class DoFlowMirrorCommand {

    @NotNull
    private List<Long> flowIds;
    private String projectType;
    private Long projectId;
    private String ownerType;
    private Long ownerId;

    public List<Long> getFlowIds() {
        return flowIds;
    }

    public void setFlowIds(List<Long> flowIds) {
        this.flowIds = flowIds;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
