// @formatter:off
package com.everhomes.rest.talent;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>ownerType: 所属类型，参考{@link com.everhomes.rest.talent.TalentOwnerType}</li>
 * <li>ownerId: 所属id</li>
 * <li>organizationId: 管理公司id</li>
 * </ul>
 */
public class ListTalentCateogryCommand {

	private String ownerType;

	private Long ownerId;

	private Long organizationId;

	public ListTalentCateogryCommand() {

	}

	public ListTalentCateogryCommand(String ownerType, Long ownerId, Long organizationId) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.organizationId = organizationId;
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

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
