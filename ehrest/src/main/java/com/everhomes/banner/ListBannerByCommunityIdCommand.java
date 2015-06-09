// @formatter:off
package com.everhomes.banner;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityId: 小区Id</li>
 * </ul>
 */
public class ListBannerByCommunityIdCommand {
    @NotNull
    private Long communityId;

    public ListBannerByCommunityIdCommand() {
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
