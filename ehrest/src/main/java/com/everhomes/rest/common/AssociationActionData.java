package com.everhomes.rest.common;

import com.everhomes.util.StringHelper;

import java.io.Serializable;


/**
 * <ul>
 *     <li>url: 路由</li>
 * </ul>
 */
public class AssociationActionData implements Serializable {

    private static final long serialVersionUID = 5229280967854473799L;
    //{"url":"zl:\/\/association\/main?layoutName=ResourceBookingLayout&itemLocation=\/rental&versionCode=2017102501&displayName=预约服务"}
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
