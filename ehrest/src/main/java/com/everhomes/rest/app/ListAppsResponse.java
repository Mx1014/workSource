package com.everhomes.rest.app;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>apps: apps</li>
 *     <li>nextPageAnchor: nextPageAnchor</li>
 * </ul>
 */
public class ListAppsResponse {

    private List<AppDTO> apps;
    private Long nextPageAnchor;

    public List<AppDTO> getApps() {
        return apps;
    }

    public void setApps(List<AppDTO> apps) {
        this.apps = apps;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
