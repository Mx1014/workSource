package com.everhomes.rest.launchpadbase.indexconfigjson;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>layoutId: layoutId</li>
 * </ul>
 */
public class Layout {

    private Long layoutId;

    private Byte layoutType;

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
