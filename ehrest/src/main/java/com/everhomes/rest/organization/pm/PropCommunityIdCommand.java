// @formatter:off
package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>communityId: 物业小区id</li>
 * </ul>
 */
public class PropCommunityIdCommand {
	@NotNull
    private Long communityId;
   
    public PropCommunityIdCommand() {
    }


	public Long getCommunityId() {
		return communityId;
	}


	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
