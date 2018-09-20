// @formatter:off
package com.everhomes.rest.launchpadbase;


import com.everhomes.rest.portal.ClientHandlerType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
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
 *     <li>cards: 卡片信息{@link OPPushCard}</li>
 *     <li>clientHandlerType: 客户端处理方式 参考{@link ClientHandlerType}</li>
 * </ul>
 */
public class ListOPPushCardsResponse {

    private Long moduleId;
    private Long appId;
    private String name;
    private Byte actionType;
    private String instanceConfig;
    private String routerPath;
    private String routerQuery;
    private Byte clientHandlerType;
    private List<OPPushCard> cards;

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

    public Byte getClientHandlerType() {
        return clientHandlerType;
    }

    public void setClientHandlerType(Byte clientHandlerType) {
        this.clientHandlerType = clientHandlerType;
    }

    public List<OPPushCard> getCards() {
        return cards;
    }

    public void setCards(List<OPPushCard> cards) {
        this.cards = cards;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
