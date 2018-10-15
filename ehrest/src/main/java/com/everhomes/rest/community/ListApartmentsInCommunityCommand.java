package com.everhomes.rest.community;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class ListApartmentsInCommunityCommand {
	
	private Integer namespaceId;
	private Long communityId;
	private Byte livingStatus;
	private String floorNumber;
	private String keyword;
	private Double areaSizeFrom;
    private Double areaSizeTo;
    private Double rentAreaFrom;
    private Double rentAreaTo;
    private Double chargeAreaFrom;
    private Double chargeAreaTo;
    private Double freeAreaFrom; 
    private Double freeAreaTo;
    private Double areaAveragePriceFrom;
    private Double areaAveragePriceTo;
    private String buildingName;
    private Integer pageSize;
    private Long pageAnchor;
    @ItemType(Long.class)
    private List<Long> communityIds;
    private Long organizationId;
    private Long appId;
    private Byte allScope;
   
	public Byte getAllScope() {
		return allScope;
	}
	public void setAllScope(Byte allScope) {
		this.allScope = allScope;
	}
	public List<Long> getCommunityIds() {
		return communityIds;
	}
	public void setCommunityIds(List<Long> communityIds) {
		this.communityIds = communityIds;
	}
	public Long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	public Long getAppId() {
		return appId;
	}
	public void setAppId(Long appId) {
		this.appId = appId;
	}
	public String getBuildingName() {
		return buildingName;
	}
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Long getPageAnchor() {
		return pageAnchor;
	}
	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
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
	public String getFloorNumber() {
		return floorNumber;
	}
	public void setFloorNumber(String floorNumber) {
		this.floorNumber = floorNumber;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
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
	public Double getAreaAveragePriceFrom() {
		return areaAveragePriceFrom;
	}
	public void setAreaAveragePriceFrom(Double areaAveragePriceFrom) {
		this.areaAveragePriceFrom = areaAveragePriceFrom;
	}
	public Double getAreaAveragePriceTo() {
		return areaAveragePriceTo;
	}
	public void setAreaAveragePriceTo(Double areaAveragePriceTo) {
		this.areaAveragePriceTo = areaAveragePriceTo;
	}
    
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
