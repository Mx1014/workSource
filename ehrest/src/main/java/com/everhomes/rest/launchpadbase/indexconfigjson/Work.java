package com.everhomes.rest.launchpadbase.indexconfigjson;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>layoutId: layoutId</li>
 * </ul>
 */
public class Work {

    private Long layoutId;

    public Long getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(Long layoutId) {
        this.layoutId = layoutId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
