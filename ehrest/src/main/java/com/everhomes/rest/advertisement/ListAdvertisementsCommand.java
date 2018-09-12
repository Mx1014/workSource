package com.everhomes.rest.advertisement;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>namespaceId: 域空间id</li>
 *  <li>communityId: 园区id</li>
 * 	<li>businessStatus: 招商状态</li>
 *  <li>advertisementType: 广告类型（1-招租广告，2-招商广告）</li>
 * 	<li>availableAreaMin: 最小招商面积</li>
 * 	<li>availableAreaMax: 最大招商面积</li>
 * 	<li>assetPriceMin: 最小租金</li>
 * 	<li>assetPriceMax: 最大租金</li>
 * 	<li>apartmentFloorMin: 最小楼层</li>
 * 	<li>apartmentFloorMax: 最大楼层</li>
 *  <li>orientation: 朝向</li>
 *  <li>keywords: 关键字</li>
 * </ul>
 */
public class ListAdvertisementsCommand {
	
	private Integer namespaceId;
	private Long communityId;
	private Byte businessStatus;
	private Byte advertisementType;
	private BigDecimal availableAreaMin;
	private BigDecimal availableAreaMax;
	private BigDecimal assetPriceMin;
	private BigDecimal assetPriceMax;
	private Integer apartmentFloorMin;
	private Integer apartmentFloorMax;
	private String orientation;
	private String keywords;
	
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
	public Byte getBusinessStatus() {
		return businessStatus;
	}
	public void setBusinessStatus(Byte businessStatus) {
		this.businessStatus = businessStatus;
	}
	public Byte getAdvertisementType() {
		return advertisementType;
	}
	public void setAdvertisementType(Byte advertisementType) {
		this.advertisementType = advertisementType;
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
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
