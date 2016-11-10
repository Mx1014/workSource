package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityId: 园区/小区id</li>
 * <li>organizationId: 公司id</li>
 * <li>orgOwnerTypeId: 业主类型</li>
 * <li>keyword: 关键字</li>
 * <li>pageAnchor: 下一页开始的锚点</li>
 * <li>pageSize: 每页大小</li>
 * <li>buildingName: 楼栋名称</li>
 * <li>addressId: 地址id</li>
 * <li>apartmentName: 门牌名称</li>
 * </ul>
 */
public class SearchOrganizationOwnersCommand {

    private Long organizationId;
    private Long communityId;
    private Long orgOwnerTypeId;
    private String keyword;
    private Long pageAnchor;
    private Integer pageSize;
    private String buildingName;
    private Long addressId;
    private String apartmentName;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getOrgOwnerTypeId() {
        return orgOwnerTypeId;
    }

    public void setOrgOwnerTypeId(Long orgOwnerTypeId) {
        this.orgOwnerTypeId = orgOwnerTypeId;
    }

    public String getKeyword() {
        return keyword;
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

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getApartmentName() {
		return apartmentName;
	}

	public void setApartmentName(String apartmentName) {
		this.apartmentName = apartmentName;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
