//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *<ul>
 * <li>billIds:账单id集合</li>
 *</ul>
 */
public class SelectedNoticeCommand {
    @ItemType(Long.class)
    private List<Long> billIds;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public SelectedNoticeCommand() {

    }
}
