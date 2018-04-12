// @formatter:off
package com.everhomes.rest.socialSecurity;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>ownerType: 所属类型 organization</li>
 * <li>ownerId: 所属id 公司id</li>
 * <li>detailIds: 人员的detailIds</li>
 * </ul>
 */
public class DecreseSocialSecurityCommand {

	private String ownerType;

	private Long ownerId;

	@ItemType(Long.class)
	private List<Long> detailIds;

	public DecreseSocialSecurityCommand() {

	}

	public DecreseSocialSecurityCommand(String ownerType, Long ownerId, List<Long> detailIds) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.detailIds = detailIds;
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

	public List<Long> getDetailIds() {
		return detailIds;
	}

	public void setDetailIds(List<Long> detailIds) {
		this.detailIds = detailIds;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
