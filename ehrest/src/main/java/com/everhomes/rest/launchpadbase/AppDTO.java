package com.everhomes.rest.launchpadbase;

import com.everhomes.rest.common.ScopeType;
import com.everhomes.rest.portal.ClientHandlerType;
import com.everhomes.util.StringHelper;


/**
 * <ul>
 *     <li>itemId: 定制版Item的Id</li>
 *     <li>scopeCode: 定制版Item的范围，参考{@link ScopeType}</li>
 *     <li>scopeId: 定制版Item的scopeId</li>
 *     <li>groupId: 运营后台的portal_item_group的Id</li>
 *     <li>name: 应用名称或者Item名称</li>
 *     <li>iconUrl: iconUrl</li>
 *     <li>itemWidth: itemWidth</li>
 *     <li>itemHeight: itemHeight</li>
 *     <li>moduleId: 模块id</li>
 *     <li>appId: 应用Id</li>
 *     <li>clientHandlerType: 客户端处理方式 参考{@link ClientHandlerType}</li>
 *     <li>routerPath: 路由路径，例如: ""</li>
 *     <li>routerQuery: 路由参数，例如: ""</li>
 *     <li>router: router</li>
 *     <li>accessControlType: accessControlType</li>
 *     <li>entryId: 应用入口ID</li>
 * </ul>
 */
public class AppDTO {

    //范围
    private Long itemId;

    private String scopeCode;

    private Long scopeId;

    private Long groupId;


    //显示
    private String name;

    private String iconUrl;

    private Integer itemWidth;

    private Integer itemHeight;


    //跳转
    private Long moduleId;

    private Long appId;

    private Byte clientHandlerType;

    private String routerPath;

    private String routerQuery;

    //路由2.0
    private String router;


    //权限控制
    private Byte accessControlType;

    private Long entryId;

    public Long getEntryId() {
        return entryId;
    }

    public void setEntryId(Long entryId) {
        this.entryId = entryId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getScopeCode() {
        return scopeCode;
    }

    public void setScopeCode(String scopeCode) {
        this.scopeCode = scopeCode;
    }

    public Long getScopeId() {
        return scopeId;
    }

    public void setScopeId(Long scopeId) {
        this.scopeId = scopeId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Integer getItemWidth() {
        return itemWidth;
    }

    public void setItemWidth(Integer itemWidth) {
        this.itemWidth = itemWidth;
    }

    public Integer getItemHeight() {
        return itemHeight;
    }

    public void setItemHeight(Integer itemHeight) {
        this.itemHeight = itemHeight;
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

    public Byte getAccessControlType() {
        return accessControlType;
    }

    public void setAccessControlType(Byte accessControlType) {
        this.accessControlType = accessControlType;
    }


    public String getRouter() {
        return router;
    }

    public void setRouter(String router) {
        this.router = router;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
