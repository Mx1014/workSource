package com.everhomes.rest.launchpadbase;

import com.everhomes.rest.module.AppCategoryDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>communityId: 园区id</li>
 *     <li>appIds: 按照顺序排列顺序传来appId</li>
 * </ul>
 */
public class UpdateUserAppsCommand {

    private Long communityId;

    private List<Long> appIds;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public List<Long> getAppIds() {
        return appIds;
    }

    public void setAppIds(List<Long> appIds) {
        this.appIds = appIds;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
