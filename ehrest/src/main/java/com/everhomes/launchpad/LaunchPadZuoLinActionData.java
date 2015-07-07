package com.everhomes.launchpad;

import com.everhomes.util.StringHelper;


/**
 * <ul>actionType为zuolin url时点击item需要的参数
 * <li>url: 左邻url</li>
 * </ul>
 */
public class LaunchPadZuoLinActionData {
    //{"url": "http://www.zuolin.com"}  
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
