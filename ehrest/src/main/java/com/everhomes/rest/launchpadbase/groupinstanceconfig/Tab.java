package com.everhomes.rest.launchpadbase.groupinstanceconfig;

import com.everhomes.rest.launchpadbase.ItemGroupDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>itemGroup: itemGroup</li>
 * </ul>
 */
public class Tab {

    private String itemGroup;


    public String getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(String itemGroup) {
        this.itemGroup = itemGroup;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
