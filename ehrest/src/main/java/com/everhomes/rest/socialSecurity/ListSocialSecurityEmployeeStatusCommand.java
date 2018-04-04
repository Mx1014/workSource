// @formatter:off
package com.everhomes.rest.socialSecurity;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 所属类型 organization</li>
 * <li>ownerId: 所属id 公司id</li>
 * <li>detailId: detail id</li>
 * <li>socialSecurityMonth: 社保月份</li>
 * </ul>
 */
public class ListSocialSecurityEmployeeStatusCommand {

	private String ownerType;

	private Long ownerId;

	private Long detailId;

	private String socialSecurityMonth;

	public ListSocialSecurityEmployeeStatusCommand() {

	}

	public ListSocialSecurityEmployeeStatusCommand(String ownerType, Long ownerId) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
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

	public String getSocialSecurityMonth() {
		return socialSecurityMonth;
	}

	public void setSocialSecurityMonth(String socialSecurityMonth) {
		this.socialSecurityMonth = socialSecurityMonth;
	}
}
