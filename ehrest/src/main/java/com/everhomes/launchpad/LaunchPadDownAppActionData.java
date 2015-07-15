package com.everhomes.launchpad;

import java.io.Serializable;

import com.everhomes.util.StringHelper;


/**
 * <ul>actionType为DOWNLOAD_APP跳转到下载APP页面
 * <li>url: 下载APP的URL（区分android/ios）</li>
 * </ul>
 */
public class LaunchPadDownAppActionData implements Serializable{

    private static final long serialVersionUID = -1378233470078843784L;
    //{"iosUrl":"https://appsto.re/cn/2Q7A7.i","andriodUrl":""}
    private String iosUrl;
    private String andriodUrl;
    
    public String getIosUrl() {
        return iosUrl;
    }

    public void setIosUrl(String iosUrl) {
        this.iosUrl = iosUrl;
    }

    public String getAndriodUrl() {
        return andriodUrl;
    }

    public void setAndriodUrl(String andriodUrl) {
        this.andriodUrl = andriodUrl;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
