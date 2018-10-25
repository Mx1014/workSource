// @formatter:off
package com.everhomes.rest.launchpadbase;


import com.everhomes.rest.portal.ClientHandlerType;
import com.everhomes.util.StringHelper;

import java.util.List;


/**
 * <ul>
 *     <li>routerPath: routerPath</li>
 *     <li>routerQuery: routerQuery</li>
 *     <li>clientHandlerType: 客户端处理方式 参考{@link ClientHandlerType}</li>
 *     <li>router: router</li>
 *     <li>properties: 需要展现的一些字段，需要和客户端预定字段和顺序，推荐按照显示的从上到下、从左到右显示放置properties</li>
 * </ul>
 */
public class OPPushCard {

    private String routerPath;
    private String routerQuery;
    private Byte clientHandlerType;
    private String router;
    private List<Object> properties;


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

    public List<Object> getProperties() {
        return properties;
    }

    public void setProperties(List<Object> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
