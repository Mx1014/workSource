// @formatter:off
package com.everhomes.rest.launchpad;

import java.util.List;



import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>itemIds: itemId列表</li>
 * </ul>
 */
public class DeleteLaunchPadItemCommand {
    @ItemType(Long.class)
    private List<Long> itemIds;

    
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
