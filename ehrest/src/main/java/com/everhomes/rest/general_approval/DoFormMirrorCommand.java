package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 *     <li>formIds: 表单 id 列表</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>projectType: projectType</li>
 *     <li>projectId: projectId</li>
 *     <li>moduleType: moduleType</li>
 *     <li>moduleId: moduleId</li>
 *     <li>ownerType: ownerType</li>
 *     <li>ownerId: ownerId</li>
 * </ul>
 */
public class DoFormMirrorCommand {

    @NotNull
    private List<Long> formIds;
    private Integer namespaceId;
    private String projectType;
    private Long projectId;
    private String moduleType;
    private Long moduleId;
    private String ownerType;
    private Long ownerId;

    public List<Long> getFormIds() {
        return formIds;
    }

    public void setFormIds(List<Long> formIds) {
        this.formIds = formIds;
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

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
