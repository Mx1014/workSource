package com.everhomes.rest.organization;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>parentId: 父级id</li>
 *     <li>childIds: 子集id集合</li>
 * </ul>
 */
public class SortOrganizationsAtSameLevelCommand {
    private Long  parentId;
    @ItemType(Long.class)
    private List<Long> childIds;

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public List<Long> getChildIds() {
        return childIds;
    }

    public void setChildIds(List<Long> childIds) {
        this.childIds = childIds;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
