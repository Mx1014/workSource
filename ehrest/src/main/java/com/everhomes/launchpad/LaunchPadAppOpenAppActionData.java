package com.everhomes.launchpad;

import java.io.Serializable;


import com.everhomes.util.StringHelper;


/**
 * <ul>actionType为APPOPEN_APP时启动第三方应用
 * <li>platform: 平台/li>
 * <li>embedded_json: 参数/li>
 * </ul>
 */
public class LaunchPadAppOpenAppActionData implements Serializable{

    private static final long serialVersionUID = -5364366676212368720L;
    //{"platform": "","embedded_json":""}  
    private String flatform;
    private String embedded_json;

    public String getFlatform() {
        return flatform;
    }

    public void setFlatform(String flatform) {
        this.flatform = flatform;
    }

    public String getEmbedded_json() {
        return embedded_json;
    }

    public void setEmbedded_json(String embedded_json) {
        this.embedded_json = embedded_json;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
