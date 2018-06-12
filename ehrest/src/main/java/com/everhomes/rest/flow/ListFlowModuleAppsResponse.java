package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>apps: 应用列表 {@link com.everhomes.rest.flow.FlowModuleAppDTO}</li>
 * </ul>
 */
public class ListFlowModuleAppsResponse {

    private List<FlowModuleAppDTO> apps;

    public List<FlowModuleAppDTO> getApps() {
        return apps;
    }

    public void setApps(List<FlowModuleAppDTO> apps) {
        this.apps = apps;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
