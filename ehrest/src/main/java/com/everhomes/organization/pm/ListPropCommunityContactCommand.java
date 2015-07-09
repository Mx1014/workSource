// @formatter:off
package com.everhomes.organization.pm;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityId: 物业小区id</li>
 * </ul>
 */
public class ListPropCommunityContactCommand {
    private Long communityId;
   
    public ListPropCommunityContactCommand() {
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
