// @formatter:off
package com.everhomes.rest.address;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.organization.pm.PropFamilyDTO;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * 	<li>aptCount: 公寓总数</li>
 * 	<li>userCount: 小区入住的总用户总数</li>
 * 	<li>liveCount: 业主自住总数</li>
 * 	<li>rentCount: 出租总数</li>
 * 	<li>freeCount: 待租总数</li>
 * 	<li>saledCount: 已售总数</li>
 * 	<li>unsaleCount: 待售总数</li>
 * 	<li>defaultCount: 其他未知总数</li>
 * 	<li>resultList: 门牌列表，参考{@link com.everhomes.rest.organization.pm.PropFamilyDTO}</li>
 * </ul>
 */
public class ListPropApartmentsResponse {
	private Integer aptCount;
	private Integer userCount;
	private Integer liveCount;
	private Integer rentCount;
	private Integer freeCount;
	private Integer saledCount;
	private Integer unsaleCount;
	private Integer defaultCount;
	private Integer hasOwnerCount;
	private Integer noOwnerCount;
	private Integer occupiedCount;
	private Integer signedUpCount;
	private Integer waitingRoomCount;
	@ItemType(PropFamilyDTO.class)
	private List<PropFamilyDTO> resultList;

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

	public Integer getOccupiedCount() {
		return occupiedCount;
	}

	public void setOccupiedCount(Integer occupiedCount) {
		this.occupiedCount = occupiedCount;
	}

	public Integer getAptCount() {
		return aptCount;
	}

	public void setAptCount(Integer aptCount) {
		this.aptCount = aptCount;
	}

	public Integer getUserCount() {
		return userCount;
	}

	public void setUserCount(Integer userCount) {
		this.userCount = userCount;
	}

	public Integer getLiveCount() {
		return liveCount;
	}

	public void setLiveCount(Integer liveCount) {
		this.liveCount = liveCount;
	}

	public Integer getRentCount() {
		return rentCount;
	}

	public void setRentCount(Integer rentCount) {
		this.rentCount = rentCount;
	}

	public Integer getFreeCount() {
		return freeCount;
	}

	public void setFreeCount(Integer freeCount) {
		this.freeCount = freeCount;
	}

	public Integer getSaledCount() {
		return saledCount;
	}

	public void setSaledCount(Integer saledCount) {
		this.saledCount = saledCount;
	}

	public Integer getUnsaleCount() {
		return unsaleCount;
	}

	public void setUnsaleCount(Integer unsaleCount) {
		this.unsaleCount = unsaleCount;
	}

	public Integer getDefaultCount() {
		return defaultCount;
	}

	public void setDefaultCount(Integer defaultCount) {
		this.defaultCount = defaultCount;
	}

	public Integer getHasOwnerCount() {
		return hasOwnerCount;
	}

	public void setHasOwnerCount(Integer hasOwnerCount) {
		this.hasOwnerCount = hasOwnerCount;
	}

	public Integer getNoOwnerCount() {
		return noOwnerCount;
	}

	public void setNoOwnerCount(Integer noOwnerCount) {
		this.noOwnerCount = noOwnerCount;
	}

	public List<PropFamilyDTO> getResultList() {
		return resultList;
	}

	public void setResultList(List<PropFamilyDTO> resultList) {
		this.resultList = resultList;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
