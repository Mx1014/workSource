// @formatter:off
package com.everhomes.rest.launchpad;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

public class ListWorkPlatformAppResponse {

    @ItemType(WorkPlatformAppDTO.class)
    private List<WorkPlatformAppDTO> apps;

    public List<WorkPlatformAppDTO> getApps() {
        return apps;
    }

    public void setApps(List<WorkPlatformAppDTO> apps) {
        this.apps = apps;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
