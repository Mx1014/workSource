package com.everhomes.rest.acl;


import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>appCategoryId: appCategoryId</li>
 *     <li>entryIds: 按照顺序传来排序后的入口Id</li>
 * </ul>
 */
public class ReorderServiceModuleEntriesCommand {

    private Long appCategoryId;

    private List<Long> entryIds;

    public Long getAppCategoryId() {
        return appCategoryId;
    }

    public void setAppCategoryId(Long appCategoryId) {
        this.appCategoryId = appCategoryId;
    }

    public List<Long> getEntryIds() {
        return entryIds;
    }

    public void setEntryIds(List<Long> entryIds) {
        this.entryIds = entryIds;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
