package com.everhomes.rest.acl;


import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>appCategoryId: 修改的到的目标分类</li>
 *     <li>entryIds: entryIds</li>
 * </ul>
 */
public class ChangeServiceModuleEntryCategoryCommand {

    private Byte appCategoryId;

    private List<Long> entryIds;

    public Byte getAppCategoryId() {
        return appCategoryId;
    }

    public void setAppCategoryId(Byte appCategoryId) {
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
