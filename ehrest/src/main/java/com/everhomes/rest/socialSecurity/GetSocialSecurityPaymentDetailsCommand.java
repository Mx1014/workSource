// @formatter:off
package com.everhomes.rest.socialSecurity;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>ownerType: 所属类型 organization</li>
 * <li>ownerId: 所属id 公司id</li>
 * <li>detailId: 人员的detailId</li>
 * </ul>
 */
public class GetSocialSecurityPaymentDetailsCommand {

	private String ownerType;

	private Long ownerId;

	private Long detailId;

	public GetSocialSecurityPaymentDetailsCommand() {

	}

	public GetSocialSecurityPaymentDetailsCommand(String ownerType, Long ownerId, Long detailId) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.detailId = detailId;
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

	public Long getDetailId() {
		return detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
