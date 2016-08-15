package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityId: 小区id</li>
 * <li>id: 主键id</li>
 * </ul>
 */
public class DeletePropOwnerAddressCommand {
	
	private Long communityId;
	
	private Long id;

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
