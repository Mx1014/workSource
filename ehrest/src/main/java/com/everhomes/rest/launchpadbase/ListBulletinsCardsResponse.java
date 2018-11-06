// @formatter:off
package com.everhomes.rest.launchpadbase;


import com.everhomes.rest.portal.ClientHandlerType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>moduleId: moduleId</li>
 *     <li>appId: appId</li>
 *     <li>name: name</li>
 *     <li>actionType: actionType</li>
 *     <li>instanceConfig: instanceConfig</li>
 *     <li>routerPath: routerPath</li>
 *     <li>routerQuery: routerQuery</li>
 *     <li>router: router</li>
 *     <li>clientHandlerType: 客户端处理方式 参考{@link ClientHandlerType}</li>
 *     <li>cards: 卡片信息{@link BulletinsCard}</li>
 * </ul>
 */
public class ListBulletinsCardsResponse {

    private Long moduleId;
    private Long appId;
    private String name;
    private Byte actionType;
    private String instanceConfig;
    private String routerPath;
    private String routerQuery;
    private String router;
    private Byte clientHandlerType;
    private List<BulletinsCard> cards;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getRouter() {
        return router;
    }

    public void setRouter(String router) {
        this.router = router;
    }

    public Byte getClientHandlerType() {
        return clientHandlerType;
    }

    public void setClientHandlerType(Byte clientHandlerType) {
        this.clientHandlerType = clientHandlerType;
    }

    public List<BulletinsCard> getCards() {
        return cards;
    }

    public void setCards(List<BulletinsCard> cards) {
        this.cards = cards;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
