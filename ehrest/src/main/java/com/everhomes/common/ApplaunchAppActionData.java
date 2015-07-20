package com.everhomes.common;

import java.io.Serializable;


import com.everhomes.util.StringHelper;


/**
 * <ul>actionType为APPOPEN_APP时启动第三方应用
 * <li>iosEmbedded_json: IOS平台打开应用需要的参数/li>
 * <li>andriodEmbedded_json: Android平台打开应用需要的参数/li>
 * </ul>
 */
public class ApplaunchAppActionData implements Serializable{

    private static final long serialVersionUID = -5364366676212368720L;
    //{"iosEmbedded_json":{"package":"mqq:open","download":"www.xx.com"},"androidEmbedded_json":{"package":"mqq:open","download":"www.xx.com"}}
    private String iosEmbedded_json;
    private String androidEmbedded_json;
    
    public String getIosEmbedded_json() {
        return iosEmbedded_json;
    }

    public void setIosEmbedded_json(String iosEmbedded_json) {
        this.iosEmbedded_json = iosEmbedded_json;
    }

    public String getAndroidEmbedded_json() {
        return androidEmbedded_json;
    }

    public void setAndroidEmbedded_json(String androidEmbedded_json) {
        this.androidEmbedded_json = androidEmbedded_json;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
