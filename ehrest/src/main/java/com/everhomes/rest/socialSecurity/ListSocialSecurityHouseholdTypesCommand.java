// @formatter:off
package com.everhomes.rest.socialSecurity;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>ownerType: 所属类型 organization</li>
 * <li>ownerId: 所属id 公司id</li>
 * <li>cityId: cityId</li>
 * </ul>
 */
public class ListSocialSecurityHouseholdTypesCommand {

	private String ownerType;

	private Long ownerId;

	private Long cityId;

	public ListSocialSecurityHouseholdTypesCommand() {

	}

	public ListSocialSecurityHouseholdTypesCommand(String ownerType, Long ownerId, Long cityId) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.cityId = cityId;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
