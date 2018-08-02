package com.everhomes.rest.acl;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>moduleId: moduleId</li>
 *     <li>appCategoryId: appCategoryId</li>
 * </ul>
 */
public class ListServiceModuleEntriesCommand {
    private Long moduleId;

    private Long appCategoryId;

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Long getAppCategoryId() {
        return appCategoryId;
    }

    public void setAppCategoryId(Long appCategoryId) {
        this.appCategoryId = appCategoryId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
