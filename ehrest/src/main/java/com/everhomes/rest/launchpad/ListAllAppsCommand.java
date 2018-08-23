// @formatter:off
package com.everhomes.rest.launchpad;


import com.everhomes.rest.launchpadbase.AppContext;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>groupId: groupId</li>
 *     <li>scopeCode: scopeCode</li>
 *     <li>scopeId: scopeId</li>
 * </ul>
 */
public class ListAllAppsCommand {

    private Long groupId;


    private AppContext appContext;



    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public AppContext getAppContext() {
        return appContext;
    }

    public void setAppContext(AppContext appContext) {
        this.appContext = appContext;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
