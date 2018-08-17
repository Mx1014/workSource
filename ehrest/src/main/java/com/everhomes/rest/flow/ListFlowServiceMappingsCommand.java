package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>flowId: the config flow id</li>
 *     <li>projectType: 项目类型, 默认为: EhCommunities</li>
 *     <li>projectId: 项目id, 一般来说是小区id</li>
 *     <li>moduleType: 模块类型, 一般来说为 any-module</li>
 *     <li>moduleId: 模块id</li>
 *     <li>ownerType: 归属类型</li>
 *     <li>ownerId: 归属id</li>
 * </ul>
 */
public class ListFlowServiceMappingsCommand {

    @NotNull
    private Long flowId;
    private String projectType;
    private Long projectId;
    private String moduleType;
    private Long moduleId;
    private String ownerType;
    private Long ownerId;

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

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
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

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
