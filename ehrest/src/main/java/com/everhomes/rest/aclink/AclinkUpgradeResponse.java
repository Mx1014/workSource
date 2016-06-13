package com.everhomes.rest.aclink;

public class AclinkUpgradeResponse {
    private Long     id;
    private String     infoUrl;
    private String     downloadUrl;
    private Long     creatorId;
    private String message;
    private String version;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getInfoUrl() {
        return infoUrl;
    }
    public void setInfoUrl(String infoUrl) {
        this.infoUrl = infoUrl;
    }
    public String getDownloadUrl() {
        return downloadUrl;
    }
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
    public Long getCreatorId() {
        return creatorId;
    }
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    
}
