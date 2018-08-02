package com.everhomes.rest.acl;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>appType: appType，参考{@link com.everhomes.rest.module.ServiceModuleAppType}</li>
 * </ul>
 */
public class ListAppCategoryCommand {

    private Byte appType;

    public Byte getAppType() {
        return appType;
    }

    public void setAppType(Byte appType) {
        this.appType = appType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
