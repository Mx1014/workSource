package com.everhomes.rest.common;

import java.io.Serializable;

import com.everhomes.util.StringHelper;


/**
 * <ul>actionType为zuolin url时点击item需要的参数
 * <li>url: 左邻url</li>
 * </ul>
 */
public class OfficialActionData implements Serializable{

    private static final long serialVersionUID = -22341699932739557L;
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
