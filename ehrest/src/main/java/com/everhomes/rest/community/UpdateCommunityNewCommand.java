package com.everhomes.rest.community;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>communityId: 园区id</li>
 *     <li>communityName: 园区名称</li>
 *     <li>aliasName: 简称</li>
 *     <li>cityId: 城市id</li>
 *     <li>cityName: 城市名称</li>
 *     <li>areaId: 区县id</li>
 *     <li>areaName: 区县名称</li>
 *     <li>address: 地址</li>
 *     <li>areaSize: 建筑面积</li>
 *     <li>rentArea: 在租面积</li>
 *     <li>freeArea: 可招租面积</li>
 *     <li>chargeArea: 收费面积</li>
 *     <li>categoryName: 分类名称</li>
 *     <li>categoryId: 分类id</li>
 *     <li>resourceType: 资源实体类型 小区园区：EhCommunities</li>
 *     <li>namespaceId: 域空间id</li>
 *     <li>communityNumber: 项目编号</li>
 *     <li><li>organizationId: 管理公司id，用于权限校验</li> </li>
 * </ul>
 */
public class UpdateCommunityNewCommand {
	
	private Long communityId;
	private String communityName;
	private String aliasName;
    private Long cityId;
    private String cityName;
    private Long areaId;
    private String areaName;
    private String address;
    private Double areaSize;
	private Double rentArea;
	private Double freeArea;
	private Double chargeArea;
    private String categoryName;
    private Long categoryId;
    private String resourceType;
    private Integer namespaceId;
    private String communityNumber;
    private Long organizationId;
    
	public Long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	public String getCommunityNumber() {
		return communityNumber;
	}
	public void setCommunityNumber(String communityNumber) {
		this.communityNumber = communityNumber;
	}
    
	public Long getCommunityId() {
		return communityId;
	}
	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	public String getCommunityName() {
		return communityName;
	}
	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}
	public String getAliasName() {
		return aliasName;
	}
	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}
	public Long getCityId() {
		return cityId;
	}
	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public Long getAreaId() {
		return areaId;
	}
	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Double getAreaSize() {
		return areaSize;
	}
	public void setAreaSize(Double areaSize) {
		this.areaSize = areaSize;
	}
	public Double getRentArea() {
		return rentArea;
	}
	public void setRentArea(Double rentArea) {
		this.rentArea = rentArea;
	}
	public Double getFreeArea() {
		return freeArea;
	}
	public void setFreeArea(Double freeArea) {
		this.freeArea = freeArea;
	}
	public Double getChargeArea() {
		return chargeArea;
	}
	public void setChargeArea(Double chargeArea) {
		this.chargeArea = chargeArea;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
    @Override
    public String toString() {
    	return StringHelper.toJsonString(this);
    }
}
