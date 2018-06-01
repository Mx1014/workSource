package com.everhomes.rest.launchpadbase;

import com.everhomes.rest.acl.AppEntryInfoDTO;
import com.everhomes.rest.portal.ClientHandlerType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>appId: appId</li>
 *     <li>name: 模块应用名称</li>
 *     <li>moduleId: 模块id</li>
 *     <li>instanceConfig: 参数json</li>
 *     <li>actionType: actionType</li>
 *     <li>actionData: actionData</li>
 *     <li>routerPath: 路由路径，例如: ""</li>
 *     <li>routerQuery: 路由参数，例如: ""</li>
 *     <li>moduleName: 模块名称</li>
 *     <li>clientHandlerType: 客户端处理方式 参考{@link ClientHandlerType}</li>
 *     <li>iconUrl: iconUrl</li>
 * </ul>
 */
public class AppDTO {

    //给客户端用的对象，减少字段传输
    private Long appId;
    private String name;
    private Long moduleId;
    private String instanceConfig;
    private Byte actionType;
    private String actionData;
    private String routerPath;
    private String routerQuery;
    private String moduleName;
    private Byte clientHandlerType;
    private String iconUrl;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Byte getClientHandlerType() {
        return clientHandlerType;
    }

    public void setClientHandlerType(Byte clientHandlerType) {
        this.clientHandlerType = clientHandlerType;
    }

    public Byte getActionType() {
        return actionType;
    }

    public void setActionType(Byte actionType) {
        this.actionType = actionType;
    }

    public String getInstanceConfig() {
        return instanceConfig;
    }

    public void setInstanceConfig(String instanceConfig) {
        this.instanceConfig = instanceConfig;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }


    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getActionData() {
        return actionData;
    }

    public void setActionData(String actionData) {
        this.actionData = actionData;
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
