package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 目前默认是： EhOrganizations</li>
 * <li>ownerId: 目前是 organizationId </li>
 * </ul>
 * @author janson
 *
 */
public class ListApprovalFormsCommand {
	private Long ownerId;
	private Long ownerType;

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Long getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(Long ownerType) {
		this.ownerType = ownerType;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
