package com.everhomes.rest.common;

import java.io.Serializable;

import com.everhomes.util.StringHelper;


/**
 * <ul>actionType为DOWNLOAD_APP跳转到下载APP页面
 * <li>iosUrl: IOS下载APP的URL</li>
 * <li>androidUrl: Android下载APP的URL</li>
 * </ul>
 */
public class DownAppActionData implements Serializable{

    private static final long serialVersionUID = -1378233470078843784L;
    //{"iosUrl":"https://appsto.re/cn/2Q7A7.i","androidUrl":""}
    private String iosUrl;
    private String androidUrl;
    
    public String getIosUrl() {
        return iosUrl;
    }

    public void setIosUrl(String iosUrl) {
        this.iosUrl = iosUrl;
    }

    public String getAndroidUrl() {
        return androidUrl;
    }

    public void setAndroidUrl(String androidUrl) {
        this.androidUrl = androidUrl;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
