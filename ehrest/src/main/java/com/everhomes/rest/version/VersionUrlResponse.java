package com.everhomes.rest.version;

public class VersionUrlResponse {
    private String downloadUrl;
    private String infoUrl;
    
    public VersionUrlResponse() {
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getInfoUrl() {
        return infoUrl;
    }

    public void setInfoUrl(String infoUrl) {
        this.infoUrl = infoUrl;
    }
}
