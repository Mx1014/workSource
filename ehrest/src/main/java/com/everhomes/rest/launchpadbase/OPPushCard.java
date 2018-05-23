// @formatter:off
package com.everhomes.rest.launchpadbase;


import com.everhomes.util.StringHelper;

import java.util.List;


/**
 * <ul>
 *     <li>routerJson: 内容的路由信息，一般是通过Id路由的。因为有些内容的id是String，有些内容的跳转路由不止一个参数。因此此处使用json，由具体业务后台和客户端约定具体内容。</li>
 *     <li>properties: 需要展现的一些字段，需要和客户端预定字段和顺序，推荐按照显示的从上到下、从左到右显示放置properties</li>
 * </ul>
 */
public class OPPushCard {

    private String routerJson;

    private List<Object> properties;

    public String getRouterJson() {
        return routerJson;
    }

    public void setRouterJson(String routerJson) {
        this.routerJson = routerJson;
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
