package com.everhomes.rest.launchpad;

import com.everhomes.rest.launchpadbase.AppContext;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>groupId: 组件Id</li>
 *     <li>appContext: 上下文信息，参考{@link AppContext}</li>
 *     <li>itemIds: 按照顺序排列顺序传来itemId</li>
 * </ul>
 */
public class UpdateUserAppsCommand {

    private Long groupId;

    private AppContext appContext;

    private List<Long> itemIds;

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

    public List<Long> getItemIds() {
        return itemIds;
    }

    public void setItemIds(List<Long> itemIds) {
        this.itemIds = itemIds;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
