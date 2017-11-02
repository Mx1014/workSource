package com.everhomes.rest.acl;

import com.everhomes.util.StringHelper;

public class ModuleAppTarget {
    private Long moduleId;
    private Long appId;
    private String controlType;

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getControlType() {
        return controlType;
    }

    public void setControlType(String controlType) {
        this.controlType = controlType;
    }
}
