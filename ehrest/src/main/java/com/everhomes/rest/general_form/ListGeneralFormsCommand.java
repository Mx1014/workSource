package com.everhomes.rest.general_form;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 目前默认是： EhOrganizations</li>
 * <li>ownerId: 目前是 organizationId </li>
 * </ul>
 * @author janson
 *
 */
public class ListGeneralFormsCommand {
	private Long ownerId;
	private String ownerType;

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

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
}
