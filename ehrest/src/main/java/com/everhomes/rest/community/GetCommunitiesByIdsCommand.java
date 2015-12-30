// @formatter:off
package com.everhomes.rest.community;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <p>
 * <ul>
 * <li>ids: 小区Id列表</li>
 * </ul>
 */
public class GetCommunitiesByIdsCommand {
    @NotNull
    @ItemType(Long.class)
    private List<Long> ids;
    
    public GetCommunitiesByIdsCommand() {
    }

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
