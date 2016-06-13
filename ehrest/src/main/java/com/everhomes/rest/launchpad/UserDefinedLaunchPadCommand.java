// @formatter:off
package com.everhomes.rest.launchpad;

import java.util.List;




import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>Items: 自定义item列表，参考 {@link com.everhomes.rest.launchpad.Item}</li>
 * </ul>
 */
public class UserDefinedLaunchPadCommand {
    
    @ItemType(Item.class)
    private List<Item> Items;

    public UserDefinedLaunchPadCommand() {
    }

    public List<Item> getItems() {
        return Items;
    }

    public void setItems(List<Item> items) {
        Items = items;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
