// @formatter:off
package com.everhomes.rest.address;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 组织id</li>
 * <li>communityId: 园区id</li>
 * <li>buildingName: 楼栋名称</li>
 * <li>apartmentFloor: 楼层</li>
 * <li>keyword: 查询关键字</li>
 * <li>namespaceId: 域空间id</li>
 * <li>livingStatus: 房源状态</li>
 * <li>areaSizeFrom: 建筑面积起点（查询条件）</li>
 * <li>areaSizeTo: 建筑面积终点（查询条件）</li>
 * <li>rentAreaFrom: 在租面积起点（查询条件）</li>
 * <li>rentAreaTo: 在租面积终点（查询条件）</li>
 * <li>chargeAreaFrom: 收费面积起点（查询条件）</li>
 * <li>chargeAreaTo: 收费面积终点（查询条件）</li>
 * <li>freeAreaFrom: 可招租面积起点（查询条件）</li>
 * <li>freeAreaTo: 可招租面积终点（查询条件） </li>
 * </ul>
 */
public class ListPropApartmentsByKeywordCommand {
    @NotNull
    private Long organizationId;
    private Long communityId;
	@NotNull
    private String buildingName;
	private Long buildingId;
	private String apartmentFloor;
    private String keyword;
    private Integer namespaceId;
    private Byte livingStatus;
    private Double areaSizeFrom;
    private Double areaSizeTo;
    private Double rentAreaFrom;
    private Double rentAreaTo;
    private Double chargeAreaFrom;
    private Double chargeAreaTo;
    private Double freeAreaFrom; 
    private Double freeAreaTo;
    
	public Long getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
	}

	public Double getRentAreaFrom() {
		return rentAreaFrom;
	}

	public void setRentAreaFrom(Double rentAreaFrom) {
		this.rentAreaFrom = rentAreaFrom;
	}

	public Double getRentAreaTo() {
		return rentAreaTo;
	}

	public void setRentAreaTo(Double rentAreaTo) {
		this.rentAreaTo = rentAreaTo;
	}

	public Double getChargeAreaFrom() {
		return chargeAreaFrom;
	}

	public void setChargeAreaFrom(Double chargeAreaFrom) {
		this.chargeAreaFrom = chargeAreaFrom;
	}

	public Double getChargeAreaTo() {
		return chargeAreaTo;
	}

	public void setChargeAreaTo(Double chargeAreaTo) {
		this.chargeAreaTo = chargeAreaTo;
	}

	public Double getFreeAreaFrom() {
		return freeAreaFrom;
	}

	public void setFreeAreaFrom(Double freeAreaFrom) {
		this.freeAreaFrom = freeAreaFrom;
	}

	public Double getFreeAreaTo() {
		return freeAreaTo;
	}

	public void setFreeAreaTo(Double freeAreaTo) {
		this.freeAreaTo = freeAreaTo;
	}

	public Double getAreaSizeFrom() {
		return areaSizeFrom;
	}

	public void setAreaSizeFrom(Double areaSizeFrom) {
		this.areaSizeFrom = areaSizeFrom;
	}

	public Double getAreaSizeTo() {
		return areaSizeTo;
	}

	public void setAreaSizeTo(Double areaSizeTo) {
		this.areaSizeTo = areaSizeTo;
	}

	public Byte getLivingStatus() {
		return livingStatus;
	}

	public void setLivingStatus(Byte livingStatus) {
		this.livingStatus = livingStatus;
	}

	public ListPropApartmentsByKeywordCommand() {
    }

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getApartmentFloor() {
		return apartmentFloor;
	}

	public void setApartmentFloor(String apartmentFloor) {
		this.apartmentFloor = apartmentFloor;
	}

	public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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
