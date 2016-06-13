// @formatter:off
package com.everhomes.rest.launchpad.admin;

import java.util.List;



import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ids: id列表</li>
 * </ul>
 */
public class DeleteLaunchPadLayoutAdminCommand {
    @ItemType(Long.class)
    private List<Long> ids;

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
