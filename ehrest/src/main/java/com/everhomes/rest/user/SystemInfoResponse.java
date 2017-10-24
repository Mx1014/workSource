package com.everhomes.rest.user;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>contentServer: 内容服务器地址。如果用户为登录，则此字段不返回</li>
 * <li>uploadUrlInBrowser: 通过 pc 上传文件的 url 链接地址</li>
 * <li>accessPoints: borderServer 的链接地址。如果用户不登录，则此地址不返回</li>
 * </ul>
 * @author janson
 *
 */
public class SystemInfoResponse {
    private String contentServer;
    private String uploadUrlInBrowser;

    @ItemType(String.class)
    private List<String> accessPoints;

    public String getContentServer() {
        return contentServer;
    }

    public void setContentServer(String contentServer) {
        this.contentServer = contentServer;
    }

    public String getUploadUrlInBrowser() {
        return uploadUrlInBrowser;
    }

    public void setUploadUrlInBrowser(String uploadUrlInBrowser) {
        this.uploadUrlInBrowser = uploadUrlInBrowser;
    }

    public List<String> getAccessPoints() {
        return accessPoints;
    }

    public void setAccessPoints(List<String> accessPoints) {
        this.accessPoints = accessPoints;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
