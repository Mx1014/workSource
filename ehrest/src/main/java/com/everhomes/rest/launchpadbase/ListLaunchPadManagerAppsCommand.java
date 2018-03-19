// @formatter:off
package com.everhomes.rest.launchpadbase;


import com.everhomes.rest.ui.user.SceneType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>communityId: 小区id</li>
 *     <li>orgId: orgId</li>
 * </ul>
 */
public class ListLaunchPadManagerAppsCommand {

    @NotNull
    private Long communityId;

    private Long orgId;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
