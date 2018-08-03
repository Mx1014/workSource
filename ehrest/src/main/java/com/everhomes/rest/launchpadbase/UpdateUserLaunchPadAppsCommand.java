package com.everhomes.rest.launchpadbase;

import com.everhomes.rest.acl.AppCategoryDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>communityId: 园区id</li>
 *     <li>appIds: 按照顺序排列顺序传来appId</li>
 * </ul>
 */
public class UpdateUserLaunchPadAppsCommand {

    private Long communityId;

    private List<Long> appIds;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
