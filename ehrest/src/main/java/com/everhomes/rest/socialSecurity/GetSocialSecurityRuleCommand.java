// @formatter:off
package com.everhomes.rest.socialSecurity;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>ownerType: 所属类型 organization</li>
 * <li>ownerId: 所属id 公司id</li>
 * <li>detailId: 人员的detailId</li>
 * <li>cityId: 城市id</li>
 * <li>householdType: 户籍类型</li>
 * <li>accumOrsocial: 社保还是公积金:1-社保 2-公积金</li>
 * </ul>
 */
public class GetSocialSecurityRuleCommand {

	private String ownerType;

	private Long ownerId;

	private Long cityId;

	private String householdType;

	private Byte accumOrsocial;

	private Long detailId;

	public Byte getAccumOrsocial() {
		return accumOrsocial;
	}

	public void setAccumOrsocial(Byte accumOrsocial) {
		this.accumOrsocial = accumOrsocial;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public String getHouseholdType() {
		return householdType;
	}

	public void setHouseholdType(String householdType) {
		this.householdType = householdType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getDetailId() {
		return detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}
}
