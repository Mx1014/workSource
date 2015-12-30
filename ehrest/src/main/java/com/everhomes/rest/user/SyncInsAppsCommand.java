package com.everhomes.rest.user;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * @author elians appInfos:{@link AppInfo}
 */
public class SyncInsAppsCommand {
    @ItemType(AppInfo.class)
    private List<AppInfo> appInfos;

    public List<AppInfo> getAppInfos() {
        return appInfos;
    }

    public void setAppInfos(List<AppInfo> appInfos) {
        this.appInfos = appInfos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
