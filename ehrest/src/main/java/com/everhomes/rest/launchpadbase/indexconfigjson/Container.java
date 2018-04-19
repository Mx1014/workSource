package com.everhomes.rest.launchpadbase.indexconfigjson;

import com.everhomes.rest.launchpadbase.LayoutType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>layoutId: layoutId</li>
 *     <li>layoutType: layoutType 参考 {@link LayoutType}</li>
 * </ul>
 */
public class Container {

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
