package com.everhomes.rest.ui.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>defaultFlag: 是否需要默认场景(0:不需要,1:需要)</li>
 * </ul>
 */
public class ListUserRelatedScenesCommand {
    private Integer defaultFlag;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Integer getDefaultFlag() {
        return defaultFlag;
    }

    public void setDefaultFlag(Integer defaultFlag) {
        this.defaultFlag = defaultFlag;
    }
}
