// @formatter:off
package com.everhomes.rest.launchpad;


import com.everhomes.rest.launchpadbase.AppContext;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>groupId: groupId</li>
 *     <li>context: context {@link com.everhomes.rest.launchpadbase.AppContext}</li>
 * </ul>
 */
public class ListAllAppsCommand {

    private Long groupId;


    private AppContext context;


    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public AppContext getContext() {
        return context;
    }

    public void setContext(AppContext context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
