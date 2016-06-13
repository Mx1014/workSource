// @formatter:off
package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>aptCount: 公寓总数</li>
 * <li>familyCount: 有用户入住的公寓总数</li>
 * <li>userCount: 小区入住的总用户总数</li>
 * <li>liveCount: 业主自住总数</li>
 * <li>rentCount: 出租总数</li>
 * <li>freeCount: 空闲总数</li>
 * <li>decorateCount: 装修总数</li>
 * <li>unsaleCount: 待售总数</li>
 * <li>defaultCount: 其他未知总数</li>
 * </ul>
 */
public class PropAptStatisticDTO {
	private Integer aptCount;
	private Integer familyCount;
	private Integer userCount;
	private Integer liveCount;
	private Integer rentCount;
	private Integer freeCount;
	private Integer decorateCount;
	private Integer unsaleCount;
	private Integer defaultCount;
    
    public PropAptStatisticDTO() {
    }
	public Integer getAptCount() {
		return aptCount;
	}
	public void setAptCount(Integer aptCount) {
		this.aptCount = aptCount;
	}
	public Integer getFamilyCount() {
		return familyCount;
	}
	public void setFamilyCount(Integer familyCount) {
		this.familyCount = familyCount;
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
	public Integer getDecorateCount() {
		return decorateCount;
	}
	public void setDecorateCount(Integer decorateCount) {
		this.decorateCount = decorateCount;
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
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
