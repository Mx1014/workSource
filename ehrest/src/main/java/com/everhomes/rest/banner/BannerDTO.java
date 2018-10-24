package com.everhomes.rest.banner;

import com.everhomes.rest.portal.ClientHandlerType;
import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: banner ID</li>
 *     <li>namespaceId: 命名空间</li>
 *     <li>scopeType: banner可见范围类型 参考{@link com.everhomes.rest.common.ScopeType}</li>
 *     <li>scopeId: 可见范围具体Id，全国为0,城市或小区Id</li>
 *     <li>bannerLocation: banner所在路径，如/home，/home/Pm</li>
 *     <li>bannerGroup: banner所在的组，参考{@link BannerGroup}</li>
 *     <li>name: 名称</li>
 *     <li>posterPath: 图片路径</li>
 *     <li>posterUrl: 图片链接</li>
 *     <li>actionType: 动作类型，参考{@link com.everhomes.rest.launchpad.ActionType}</li>
 *     <li>actionData: 根据actionType不同的取值决定，json格式的字符串，跳圈，或直接进入帖子等等</li>
 *     <li>targetType: 跳转类型 {@link com.everhomes.rest.banner.BannerTargetType}</li>
 *     <li>targetData: 跳转类型对应的data,每种targetType对应的data都不一样</li>
 *     <li>status: status</li>
 *     <li>order: 顺序</li>
 *     <li>updateTime: 最后一次更新的时间</li>
 *     <li>moduleId: moduleId</li>
 *     <li>appId: appId</li>
 *     <li>routerPath: routerPath</li>
 *     <li>routerQuery: routerQuery</li>
 *     <li>router: router</li>
 *     <li>clientHandlerType: 客户端处理方式 参考{@link ClientHandlerType}</li>
 * </ul>
 */
public class BannerDTO {

    private Long id;
    private Integer namespaceId;
    private String scopeType;
    private Long scopeId;
    private String bannerLocation;
    private String bannerGroup;
    private String name;
    private String posterPath;
    private String posterUrl;
    private Byte actionType;
    private String actionData;
    private String targetType;
    private String targetData;
    private Byte status;
    private Integer order;
    private Timestamp updateTime;

    private Long moduleId;
    private Long appId;
    private String routerPath;
    private String routerQuery;
    private String router;
    private Byte clientHandlerType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getScopeType() {
        return scopeType;
    }

    public void setScopeType(String scopeType) {
        this.scopeType = scopeType;
    }

    public Long getScopeId() {
        return scopeId;
    }

    public void setScopeId(Long scopeId) {
        this.scopeId = scopeId;
    }

    public String getBannerLocation() {
        return bannerLocation;
    }

    public void setBannerLocation(String bannerLocation) {
        this.bannerLocation = bannerLocation;
    }

    public String getBannerGroup() {
        return bannerGroup;
    }

    public void setBannerGroup(String bannerGroup) {
        this.bannerGroup = bannerGroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public Byte getActionType() {
        return actionType;
    }

    public void setActionType(Byte actionType) {
        this.actionType = actionType;
    }

    public String getActionData() {
        return actionData;
    }

    public void setActionData(String actionData) {
        this.actionData = actionData;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getTargetData() {
        return targetData;
    }

    public void setTargetData(String targetData) {
        this.targetData = targetData;
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

    public Byte getClientHandlerType() {
        return clientHandlerType;
    }

    public void setClientHandlerType(Byte clientHandlerType) {
        this.clientHandlerType = clientHandlerType;
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
