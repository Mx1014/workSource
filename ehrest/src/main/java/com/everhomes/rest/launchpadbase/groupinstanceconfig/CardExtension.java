// @formatter:off
package com.everhomes.rest.launchpadbase.groupinstanceconfig;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>itemGroup: itemGroup</li>
 *     <li>moduleId: moduleId</li>
 *     <li>appId: appId</li>
 *     <li>clientHandlerType: clientHandlerType</li>
 *     <li>routerPath: routerPath</li>
 *     <li>routerQuery: routerQuery</li>
 * </ul>
 */
public class CardExtension {

    private String itemGroup;

    //跳转
    private Long moduleId;

    private Long appId;

    private Byte clientHandlerType;

    private String routerPath;

    private String routerQuery;

    public String getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(String itemGroup) {
        this.itemGroup = itemGroup;
    }

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

    public Byte getClientHandlerType() {
        return clientHandlerType;
    }

    public void setClientHandlerType(Byte clientHandlerType) {
        this.clientHandlerType = clientHandlerType;
    }

    public String getRouterPath() {
        return routerPath;
    }

    public void setRouterPath(String routerPath) {
        this.routerPath = routerPath;
    }

    public String getRouterQuery() {
        return routerQuery;
    }

    public void setRouterQuery(String routerQuery) {
        this.routerQuery = routerQuery;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
