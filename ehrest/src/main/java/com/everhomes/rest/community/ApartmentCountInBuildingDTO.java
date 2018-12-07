package com.everhomes.rest.community;

import com.everhomes.util.StringHelper;
/**
 *<ul>
 * 	<li>totalApartmentCount :房源总数</li>
 *	<li>freeApartmentCount : 待租房源数</li>
 *	<li>rentApartmentCount : 已出租房源数</li>
 *	<li>occupiedApartmentCount : 已占用房源数</li>
 *	<li>livingApartmentCount : 自用房源数</li>
 *	<li>saledApartmentCount : 已售房源数</li>
 *	<li>unsaleApartmentCount : 代售房源数</li>
 *	<li>defaultApartmentCount : 其他房源数</li>
 *</ul>
 *
 */
public class ApartmentCountInBuildingDTO {
	
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
