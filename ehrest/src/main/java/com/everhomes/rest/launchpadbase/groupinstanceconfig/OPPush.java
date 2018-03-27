package com.everhomes.rest.launchpadbase.groupinstanceconfig;

import com.everhomes.rest.launchpadbase.ItemGroupDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>OPPush的配置信息，现在的关键信息是itemGroup用于区分是哪个类型，其他字段以后再加</li>
 *     <li>itemGroup: itemGroup，例如：活动为OPPushActivity</li>
 * </ul>
 */
public class OPPush {

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
