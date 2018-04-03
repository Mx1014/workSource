package com.everhomes.rest.common;

import java.io.Serializable;



import com.everhomes.util.StringHelper;


/**
 * <ul>actionType为zuolin url时点击item需要的参数
 * <li>url: 左邻url</li>
 * </ul>
 */
public class ThirdPartActionData implements Serializable{

    private static final long serialVersionUID = 5331800144156878448L;
    //{"url": "http://www.baidu.com"}  
    private String url;

    private Byte declareFlag;
    
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Byte getDeclareFlag() {
        return declareFlag;
    }

    public void setDeclareFlag(Byte declareFlag) {
        this.declareFlag = declareFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
