package com.everhomes.rest.launchpad;

import com.everhomes.rest.launchpadbase.AppContext;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>groupId: 组件Id</li>
 *     <li>context: 上下文信息，其中communityId必须传。参考{@link AppContext}</li>
 *     <li>itemIds: 按照顺序排列顺序传来itemId</li>
 * </ul>
 */
public class UpdateUserAppsCommand {

    private Long groupId;

    private AppContext context;

    private List<Long> itemIds;

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
