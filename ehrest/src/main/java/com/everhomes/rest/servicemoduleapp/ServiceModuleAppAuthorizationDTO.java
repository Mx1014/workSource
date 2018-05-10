package com.everhomes.rest.servicemoduleapp;

import com.everhomes.rest.acl.AppEntryInfoDTO;

import java.util.List;

/**
 * <ul>
 *     <li>id: 模块应用id</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>ownerId: ownerId</li>
 *     <li>ownerName: ownerName</li>
 *     <li>organizationId: organizationId</li>
 *     <li>organizationName: organizationName</li>
 *     <li>projectId: projectId</li>
 *     <li>projectName: projectName</li>
 *     <li>appId: appId</li>
 *     <li>appName: appName</li>
 * </ul>
 */
public class ServiceModuleAppAuthorizationDTO {

    private Long id;
    private Integer namespaceId;
    private Long ownerId;
    private String ownerName;
    private Long organizationId;
    private String organizationName;
    private Long projectId;
    private String projectName;
    private Long appId;
    private String appName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
