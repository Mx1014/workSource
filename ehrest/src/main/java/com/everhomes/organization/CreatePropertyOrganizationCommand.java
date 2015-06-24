// @formatter:off
package com.everhomes.organization;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityId：小区</li>
 * </ul>
 */
public class CreatePropertyOrganizationCommand {
	
	@NotNull
	private Long communityId;

	

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
