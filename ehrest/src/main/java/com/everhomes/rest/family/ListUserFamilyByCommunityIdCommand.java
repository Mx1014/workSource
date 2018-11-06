// @formatter:off
package com.everhomes.rest.family;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 *     <li>communityId: communityId</li>
 * </ul>
 */
public class ListUserFamilyByCommunityIdCommand {

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
