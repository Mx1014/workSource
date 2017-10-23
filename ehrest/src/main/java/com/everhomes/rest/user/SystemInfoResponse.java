package com.everhomes.rest.user;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

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
