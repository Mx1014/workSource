package com.everhomes.rest.community;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;
/**
<ul>
<li>buildingId: 楼栋id</li>
<li>buildingName: 楼栋名称</li>
<li>address: 地址</li>
<li>areaSize: 建筑面积</li>
<li>rentArea: 在租面积</li>
<li>freeArea: 可招租面积</li>
<li>chargeArea: 收费面积</li>
<li>freeRate: 空置率</li>
<li>totalApartmentCount: 房源总数</li>
<li>freeApartmentCount: 待租房源总数</li>
<li>rentApartmentCount: 已出租房源总数</li>
<li>occupiedApartmentCount: 已占用房源总数</li>
<li>livingApartmentCount: 自用房源总数</li>
<li>saledApartmentCount: 已售房源总数</li>
<li>unsaleApartmentCount: 代售房源总数</li>
<li>defaultApartmentCount: 其他房源总数</li>
<li>signedUpCount: 待签约房源总数</li>
<li>waitingRoomCount: 待接房房源总数</li>
<li>posterUri: 楼宇图片uri</li>
<li>posterUrl: 楼宇图片url</li>
</ul>*/

public class BuildingStatisticsForAppDTO {
	
	private Long buildingId;
	private String buildingName;
	private String address;
	private Double areaSize;
	private Double rentArea;
	private Double freeArea;
	private Double chargeArea;
	private BigDecimal freeRate;
	private Integer totalApartmentCount;
	private Integer freeApartmentCount;
	private Integer rentApartmentCount;
	private Integer occupiedApartmentCount;
	private Integer livingApartmentCount;
	private Integer saledApartmentCount;
	private Integer unsaleApartmentCount;
	private Integer defaultApartmentCount;
	private Integer signedUpCount;
	private Integer waitingRoomCount;
	private String posterUri;
	private String posterUrl;
	
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
	public BigDecimal getFreeRate() {
		return freeRate;
	}
	public void setFreeRate(BigDecimal freeRate) {
		this.freeRate = freeRate;
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
	public Integer getUnsaleApartmentCount() {
		return unsaleApartmentCount;
	}
	public void setUnsaleApartmentCount(Integer unsaleApartmentCount) {
		this.unsaleApartmentCount = unsaleApartmentCount;
	}
	public Integer getDefaultApartmentCount() {
		return defaultApartmentCount;
	}
	public void setDefaultApartmentCount(Integer defaultApartmentCount) {
		this.defaultApartmentCount = defaultApartmentCount;
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
	public Integer getSignedUpCount() {
		return signedUpCount;
	}
	public void setSignedUpCount(Integer signedUpCount) {
		this.signedUpCount = signedUpCount;
	}
	public Integer getWaitingRoomCount() {
		return waitingRoomCount;
	}
	public void setWaitingRoomCount(Integer waitingRoomCount) {
		this.waitingRoomCount = waitingRoomCount;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	
}
