package com.everhomes.rest.advertisement;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

public class AdvertisementDTO {
	
	private String title;
	private Byte businessStatus;
	private BigDecimal availableAreaMin;
	private BigDecimal availableAreaMax;
	private BigDecimal assetPriceMin;
	private BigDecimal assetPriceMax;
	private Integer apartmentFloorMin;
	private Integer apartmentFloorMax;
	private String orientation;
	private Timestamp createTime;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Byte getBusinessStatus() {
		return businessStatus;
	}
	public void setBusinessStatus(Byte businessStatus) {
		this.businessStatus = businessStatus;
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
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
