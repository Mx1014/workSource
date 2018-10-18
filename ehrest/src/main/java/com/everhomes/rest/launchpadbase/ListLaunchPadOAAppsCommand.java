// @formatter:off
package com.everhomes.rest.launchpadbase;


import com.everhomes.rest.ui.user.SceneType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>orgId: orgId</li>
 * </ul>
 */
public class ListLaunchPadOAAppsCommand {

    private Long orgId;

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
