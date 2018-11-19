// @formatter:off
package com.everhomes.rest.widget;

import com.everhomes.util.StringHelper;

public class VanKeSmartCardConfig {

    private Long moduleAppId;

    public Long getModuleAppId() {
        return moduleAppId;
    }

    public void setModuleAppId(Long moduleAppId) {
        this.moduleAppId = moduleAppId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
