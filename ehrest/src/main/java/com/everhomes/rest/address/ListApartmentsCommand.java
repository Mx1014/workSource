package com.everhomes.rest.address;

import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>namespaceId: 域空间id</li>
 * <li>organizationId: 组织id</li>
 * <li>communityId: 小区id</li>
 * <li>buildingName: 楼栋号</li>
 * <li>apartment: 门牌名</li>
 * <li>livingStatus: 资产状态 参考{@link com.everhomes.rest.organization.pm.AddressMappingStatus}</li>
 * <li>pageSize: 页面大小</li>
 * <li>pageAnchor: 锚点</li>
 * <li>livingStatusList: 兼容可以同时查询多种状态房源的需求</li>
 * </ul>
 * Created by ying.xiong on 2017/8/18.
 */
public class ListApartmentsCommand {

    private Integer namespaceId;
    @NotNull
    private Long organizationId;

    private Long communityId;

    private String buildingName;

    private String apartment;

    private Byte livingStatus;

    private Integer pageSize;

    private Long pageAnchor;
    
	public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
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

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Byte getLivingStatus() {
        return livingStatus;
    }

    public void setLivingStatus(Byte livingStatus) {
        this.livingStatus = livingStatus;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }
}
