package com.everhomes.rest.launchpadbase;

import com.everhomes.rest.module.AppCategoryDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>communityId: 园区id</li>
 *     <li>appIds: 按照顺序排列顺序传来appId</li>
 *     <li>entryIds: 按照顺序排列顺序传来的entryId</li>
 * </ul>
 */
public class UpdateUserAppsCommand {

    private Long communityId;

    private List<Long> appIds;

    private List<Long> entryIds;

    private AppContext context;

    public List<Long> getEntryIds() {
        return entryIds;
    }

    public void setEntryIds(List<Long> entryIds) {
        this.entryIds = entryIds;
    }

    public AppContext getContext() {
        return context;
    }

    public void setContext(AppContext context) {
        this.context = context;
    }

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
