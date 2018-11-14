// @formatter:off
package com.everhomes.rest.messaging;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>url: 路由跳转链接</li>
 *     <li>router: 路由跳转链接2.0</li>
 * </ul>
 */
public class RouterMetaObject {

    private String url;

    private String router;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
