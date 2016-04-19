// @formatter:off
package com.everhomes.rest.ui.organization;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>communityId: 小区id</li>
 * </ul>
 *
 */
public class SetCurrentCommunityForSceneCommand {
	
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
