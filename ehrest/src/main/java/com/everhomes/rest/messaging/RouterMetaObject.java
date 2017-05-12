// @formatter:off
package com.everhomes.rest.messaging;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>url: 路由跳转链接</li>
 * </ul>
 */
public class RouterMetaObject {

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
