package com.everhomes.rest.launchpadbase;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>apps: 应用列表，参考{@link AppDTO}</li>
 * </ul>
 */
public class ListLaunchPadAppsResponse {

    private List<AppDTO> apps;

    public List<AppDTO> getApps() {
        return apps;
    }

    public void setApps(List<AppDTO> apps) {
        this.apps = apps;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
