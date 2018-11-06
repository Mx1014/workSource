package com.everhomes.rest.investmentAd;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>id: 招商广告id</li>
 *  <li>title: 招商广告标题</li>
 * 	<li>investmentStatus: 招商状态</li>
 * 	<li>investmentType: 招商类型（1-待租，2-待售）</li>
 * 	<li>availableAreaMin: 最小招商面积</li>
 * 	<li>availableAreaMax: 最大招商面积</li>
 *  <li>priceUnit: 租金面积</li>
 * 	<li>assetPriceMin: 最小租金</li>
 * 	<li>assetPriceMax: 最大租金</li>
 * 	<li>apartmentFloorMin: 最小楼层</li>
 * 	<li>apartmentFloorMax: 最大楼层</li>
 *  <li>orientation: 朝向</li>
 *  <li>createTime: 发布时间</li>
 *  <li>defaultOrder: 排序字段</li>
 * </ul>
 */
public class InvestmentAdDTO {
	
	private Long id;
	private String title;
	private Byte investmentStatus;
	private Byte investmentType;
	private BigDecimal availableAreaMin;
	private BigDecimal availableAreaMax;
	private BigDecimal assetPriceMin;
	private BigDecimal assetPriceMax;
	private Byte priceUnit;
	private Integer apartmentFloorMin;
	private Integer apartmentFloorMax;
	private String orientation;
	private Timestamp createTime;
	private Long defaultOrder;
	private String posterUri;
	private String posterUrl;
	
	public Byte getInvestmentType() {
		return investmentType;
	}
	public void setInvestmentType(Byte investmentType) {
		this.investmentType = investmentType;
	}
	public String getPosterUri() {
		return posterUri;
	}
	public void setPosterUri(String posterUri) {
		this.posterUri = posterUri;
	}
	public String getPosterUrl() {
		return posterUrl;
	}
	public void setPosterUrl(String posterUrl) {
		this.posterUrl = posterUrl;
	}
	public Byte getPriceUnit() {
		return priceUnit;
	}
	public void setPriceUnit(Byte priceUnit) {
		this.priceUnit = priceUnit;
	}
	public Long getDefaultOrder() {
		return defaultOrder;
	}
	public void setDefaultOrder(Long defaultOrder) {
		this.defaultOrder = defaultOrder;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Byte getInvestmentStatus() {
		return investmentStatus;
	}
	public void setInvestmentStatus(Byte investmentStatus) {
		this.investmentStatus = investmentStatus;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
