// @formatter:off
package com.everhomes.rest.user;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 *     <li>ids: 用户ID列表</li>
 * </ul>
 */
public class GetUserListCommand {

    @ItemType(Long.class)
    private List<Long> ids;

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}
