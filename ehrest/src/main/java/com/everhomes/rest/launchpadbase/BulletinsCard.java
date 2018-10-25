// @formatter:off
package com.everhomes.rest.launchpadbase;


import com.everhomes.rest.portal.ClientHandlerType;
import com.everhomes.util.StringHelper;

import java.util.List;


/**
 * <ul>
 *     <li>routerPath: 路由跳转路径，此处填"/detail"</li>
 *     <li>routerQuery: 路由跳转参数</li>
 *     <li>router: router</li>
 *     <li>clientHandlerType: 客户端处理方式 参考{@link ClientHandlerType}</li>
 *     <li>title: 标题，5.8.0版本的时候客户端仅显示标题</li>
 *     <li>content: 内容，暂时没有到</li>
 * </ul>
 */
public class BulletinsCard {

    private String routerPath;
    private String routerQuery;
    private String router;
    private Byte clientHandlerType;
    private String title;
    private String content;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
