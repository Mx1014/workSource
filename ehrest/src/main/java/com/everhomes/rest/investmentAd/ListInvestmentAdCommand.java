package com.everhomes.rest.investmentAd;

import java.math.BigDecimal;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>namespaceId: 域空间id</li>
 *  <li>communityId: 园区id</li>
 * 	<li>investmentStatus: 招商状态:1-招商中,2-已下线,3-已出租</li>
 *  <li>investmentType: 广告类型（1-招租广告，2-招商广告）</li>
 * 	<li>availableAreaMin: 最小招商面积</li>
 * 	<li>availableAreaMax: 最大招商面积</li>
 * 	<li>assetPriceMin: 最小租金</li>
 * 	<li>assetPriceMax: 最大租金</li>
 * 	<li>apartmentFloorMin: 最小楼层</li>
 * 	<li>apartmentFloorMax: 最大楼层</li>
 *  <li>orientation: 朝向</li>
 *  <li>keywords: 关键字</li>
 *  <li>sortField: 排序字段:defaultOrder,availableAreaMin,assetPriceMin</li>
 *  <li>sortType: 1-升序，2-降序</li>
 * </ul>
 */
public class ListInvestmentAdCommand {
	
	private Integer namespaceId;
	private Long communityId;
	private Long organizationId;
	private Byte investmentStatus;
	private Byte investmentType;
	private BigDecimal availableAreaMin;
	private BigDecimal availableAreaMax;
	private BigDecimal assetPriceMin;
	private BigDecimal assetPriceMax;
	private Integer apartmentFloorMin;
	private Integer apartmentFloorMax;
	private String orientation;
	private String keywords;
	private Integer pageSize;
	private Long PageAnchor;
	private String sortField;
	private Byte sortType;
	@ItemType(Long.class)
    private List<Long> communityIds;
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
	public Long getAppId() {
		return appId;
	}
	public void setAppId(Long appId) {
		this.appId = appId;
	}
	public Long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
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
	public Byte getInvestmentStatus() {
		return investmentStatus;
	}
	public void setInvestmentStatus(Byte investmentStatus) {
		this.investmentStatus = investmentStatus;
	}
	public Byte getInvestmentType() {
		return investmentType;
	}
	public void setInvestmentType(Byte investmentType) {
		this.investmentType = investmentType;
	}
	public BigDecimal getAvailableAreaMin() {
		return availableAreaMin;
	}
	public void setAvailableAreaMin(BigDecimal availableAreaMin) {
		this.availableAreaMin = availableAreaMin;
	}
	public BigDecimal getAvailableAreaMax() {
		return availableAreaMax;
	}
	public void setAvailableAreaMax(BigDecimal availableAreaMax) {
		this.availableAreaMax = availableAreaMax;
	}
	public BigDecimal getAssetPriceMin() {
		return assetPriceMin;
	}
	public void setAssetPriceMin(BigDecimal assetPriceMin) {
		this.assetPriceMin = assetPriceMin;
	}
	public BigDecimal getAssetPriceMax() {
		return assetPriceMax;
	}
	public void setAssetPriceMax(BigDecimal assetPriceMax) {
		this.assetPriceMax = assetPriceMax;
	}
	public Integer getApartmentFloorMin() {
		return apartmentFloorMin;
	}
	public void setApartmentFloorMin(Integer apartmentFloorMin) {
		this.apartmentFloorMin = apartmentFloorMin;
	}
	public Integer getApartmentFloorMax() {
		return apartmentFloorMax;
	}
	public void setApartmentFloorMax(Integer apartmentFloorMax) {
		this.apartmentFloorMax = apartmentFloorMax;
	}
	public String getOrientation() {
		return orientation;
	}
	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Long getPageAnchor() {
		return PageAnchor;
	}
	public void setPageAnchor(Long pageAnchor) {
		PageAnchor = pageAnchor;
	}
	public String getSortField() {
		return sortField;
	}
	public void setSortField(String sortField) {
		this.sortField = sortField;
	}
	public Byte getSortType() {
		return sortType;
	}
	public void setSortType(Byte sortType) {
		this.sortType = sortType;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
