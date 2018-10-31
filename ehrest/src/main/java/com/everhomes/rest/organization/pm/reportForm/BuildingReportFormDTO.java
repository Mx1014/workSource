package com.everhomes.rest.organization.pm.reportForm;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;
/**
 *<ul>
 * 	<li>buildingId</li>
 *	<li>buildingName：楼宇名称</li>
 *	<li>totalApartmentCount：房源总数</li>
 *	<li>freeApartmentCount：待租房源数</li>
 *	<li>rentApartmentCount：已出租房源数</li>
 *	<li>occupiedApartmentCount：已占用房源数</li>
 *	<li>livingApartmentCount：自用房源数</li>
 *	<li>saledApartmentCount：已售房源数</li>
 *	<li>areaSize：建筑面积(㎡)</li>
 *	<li>rentArea：在租面积(㎡)</li>
 *	<li>freeArea：可招租面积(㎡)</li>
 *	<li>rentRate：出租率(%)</li>
 *	<li>freeRate：空置率(%)</li>
 *	<li>amountReceivable：应收含税金额(元)</li>
 *	<li>amountReceived：已收金额(元)</li>
 *	<li>amountOwed：待收金额(元)</li>
 *	<li>dueDayCount：总欠费天数(天)</li>
 *	<li>collectionRate：收缴率(%)</li>
 *</ul>
 */
public class BuildingReportFormDTO {
	
	private Long buildingId;
	private String buildingName;
	private Integer totalApartmentCount;
	private Integer freeApartmentCount;
	private Integer rentApartmentCount;
	private Integer occupiedApartmentCount;
	private Integer livingApartmentCount;
	private Integer saledApartmentCount;
	private BigDecimal areaSize;
	private BigDecimal rentArea;
	private BigDecimal freeArea;
	private BigDecimal rentRate;
	private BigDecimal freeRate;
	private BigDecimal amountReceivable;
	private BigDecimal amountReceived;
	private BigDecimal amountOwed;
	private BigDecimal dueDayCount;
	private BigDecimal collectionRate;
	
	public Long getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
	}
	public String getBuildingName() {
		return buildingName;
	}
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	public Integer getTotalApartmentCount() {
		return totalApartmentCount;
	}
	public void setTotalApartmentCount(Integer totalApartmentCount) {
		this.totalApartmentCount = totalApartmentCount;
	}
	public Integer getFreeApartmentCount() {
		return freeApartmentCount;
	}
	public void setFreeApartmentCount(Integer freeApartmentCount) {
		this.freeApartmentCount = freeApartmentCount;
	}
	public Integer getRentApartmentCount() {
		return rentApartmentCount;
	}
	public void setRentApartmentCount(Integer rentApartmentCount) {
		this.rentApartmentCount = rentApartmentCount;
	}
	public Integer getOccupiedApartmentCount() {
		return occupiedApartmentCount;
	}
	public void setOccupiedApartmentCount(Integer occupiedApartmentCount) {
		this.occupiedApartmentCount = occupiedApartmentCount;
	}
	public Integer getLivingApartmentCount() {
		return livingApartmentCount;
	}
	public void setLivingApartmentCount(Integer livingApartmentCount) {
		this.livingApartmentCount = livingApartmentCount;
	}
	public Integer getSaledApartmentCount() {
		return saledApartmentCount;
	}
	public void setSaledApartmentCount(Integer saledApartmentCount) {
		this.saledApartmentCount = saledApartmentCount;
	}
	public BigDecimal getAreaSize() {
		return areaSize;
	}
	public void setAreaSize(BigDecimal areaSize) {
		this.areaSize = areaSize;
	}
	public BigDecimal getRentArea() {
		return rentArea;
	}
	public void setRentArea(BigDecimal rentArea) {
		this.rentArea = rentArea;
	}
	public BigDecimal getFreeArea() {
		return freeArea;
	}
	public void setFreeArea(BigDecimal freeArea) {
		this.freeArea = freeArea;
	}
	public BigDecimal getRentRate() {
		return rentRate;
	}
	public void setRentRate(BigDecimal rentRate) {
		this.rentRate = rentRate;
	}
	public BigDecimal getFreeRate() {
		return freeRate;
	}
	public void setFreeRate(BigDecimal freeRate) {
		this.freeRate = freeRate;
	}
	public BigDecimal getAmountReceivable() {
		return amountReceivable;
	}
	public void setAmountReceivable(BigDecimal amountReceivable) {
		this.amountReceivable = amountReceivable;
	}
	public BigDecimal getAmountReceived() {
		return amountReceived;
	}
	public void setAmountReceived(BigDecimal amountReceived) {
		this.amountReceived = amountReceived;
	}
	public BigDecimal getAmountOwed() {
		return amountOwed;
	}
	public void setAmountOwed(BigDecimal amountOwed) {
		this.amountOwed = amountOwed;
	}
	public BigDecimal getDueDayCount() {
		return dueDayCount;
	}
	public void setDueDayCount(BigDecimal dueDayCount) {
		this.dueDayCount = dueDayCount;
	}
	public BigDecimal getCollectionRate() {
		return collectionRate;
	}
	public void setCollectionRate(BigDecimal collectionRate) {
		this.collectionRate = collectionRate;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
