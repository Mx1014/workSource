package com.everhomes.rest.aclink;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>doorId: 门禁ID</li>
 * <li>namespaceId: 域空间ID</li>
 * <li>keyword：搜索关键字，可以是手机号或昵称</li>
 * <li>organizationId： 公司ID，通过名字找公司用 /admin/org/listOrganizationByName</li>
 * <li>buildingId: 暂时不支持,因为后台没数据 </li>
 * <li>isAuth: 认证则为1, 非认证为0</li>
 * <li>buildName: /community/listBuildings </li>
 * <li></li>
 * </ul>
 * 
 * @author janson
 *
 */
public class ListAclinkUserCommand {
    private Long pageAnchor;
    
    @NotNull
    private Long doorId;
    
    private Integer pageSize;
    
    @NotNull
    private Integer namespaceId;
    
    private String keyword;
    
    private Long organizationId;
    
    private Long buildingId;
    
    private String buildingName;
    
    private Long communityId;
    
    private Byte isAuth;
    
    private Byte isOpenAuth;

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

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
    
}
