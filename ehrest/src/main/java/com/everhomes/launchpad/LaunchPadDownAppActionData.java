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
