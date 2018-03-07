package com.everhomes.rest.banner.targetdata;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>uri: 路由</li>
 * </ul>
 */
public class BannerRouteTargetData {

    private String uri;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
