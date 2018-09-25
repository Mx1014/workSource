package com.everhomes.rest.aclink;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>doorId: 门禁ID</li>
 * <li>namespaceId: 域空间ID</li>
 * <li>keyword：搜索关键字，可以是手机号或昵称</li>
 * <li>organizationId： 公司ID，通过名字找公司用 /admin/org/listOrganizationByName</li>
 * <li>isAuth: 用户认证认证则为1, 非认证为0</li>
 * <li>isOpenAuth: 用户有门禁授权，则为1, 否则为 0</li>
 * <li>buildingName: /community/listBuildings </li>
 * <li>communityType: 小区类型 0:住宅类型小区， 1: 商用类型园区  @{link com.everhomes.rest.communityCommunityType}</li>
 * <li>communityId: 小区 ID</li>
 * <li>currentPMId: 当前管理公司ID(organizationID)</li>
 * <li>currentProjectId: 当前选中项目Id，如果是全部则不传</li>
 * <li>appId: 应用id</li>
 * <li></li>
 * </ul>
 * 
 * @author janson
 *
 */
public class ListAclinkUserCommand {
    private Long pageAnchor;
    

    private Long doorId;
    
    private Integer pageSize;
    

    private Integer namespaceId;
    
    private String keyword;
    
    private Long organizationId;
    
    private String buildingName;
    
    private Long communityId;
    
    private Byte communityType;
    
    private Byte isAuth;
    
    private Byte isOpenAuth;

    private Byte ownerType;
    //权限
    private Long appId;
    private Long currentPMId;
    private Long currentProjectId;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getCurrentPMId() {
        return currentPMId;
    }

    public void setCurrentPMId(Long currentPMId) {
        this.currentPMId = currentPMId;
    }

    public Long getCurrentProjectId() {
        return currentProjectId;
    }

    public void setCurrentProjectId(Long currentProjectId) {
        this.currentProjectId = currentProjectId;
    }

    public Byte getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Byte ownerType) {
        this.ownerType = ownerType;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Long getDoorId() {
        return doorId;
    }

    public void setDoorId(Long doorId) {
        this.doorId = doorId;
    }
    
    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Byte getIsAuth() {
        return isAuth;
    }

    public void setIsAuth(Byte isAuth) {
        this.isAuth = isAuth;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public Byte getIsOpenAuth() {
        return isOpenAuth;
    }

    public void setIsOpenAuth(Byte isOpenAuth) {
        this.isOpenAuth = isOpenAuth;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Byte getCommunityType() {
        return communityType;
    }

    public void setCommunityType(Byte communityType) {
        this.communityType = communityType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
    
}
