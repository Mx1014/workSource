package com.everhomes.rest.common;

import java.io.Serializable;



import com.everhomes.util.StringHelper;


/**
 * <ul>actionType为BIZ_DETAILS时跳转到商家详情
 * <li>url: 左邻url</li>
 * </ul>
 */
public class BizDetailActionData implements Serializable{
    private static final long serialVersionUID = 3538472620854484985L;
    //{"url": "http://www.baidu.com"}  
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
