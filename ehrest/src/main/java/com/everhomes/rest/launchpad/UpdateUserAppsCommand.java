package com.everhomes.rest.launchpad;

import com.everhomes.rest.launchpadbase.AppContext;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>scopeCode: item可见范围类型，此参数用于校验item，参考{@link com.everhomes.rest.common.ScopeType}</li>
 *     <li>scopeId: 看见范围具体Id，此参数用于校验item，0为全部</li>
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
