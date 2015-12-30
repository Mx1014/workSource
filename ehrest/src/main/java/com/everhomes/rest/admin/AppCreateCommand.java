package com.everhomes.rest.admin;

public class AppCreateCommand {
    private String appKey;
    private String creatorUid;
    private String name;
    private String secretKey;
    private String description;
    
    public String getAppKey() {
        return appKey;
    }
    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
    public String getCreatorUid() {
        return creatorUid;
    }
    public void setCreatorUid(String creatorUid) {
        this.creatorUid = creatorUid;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSecretKey() {
        return secretKey;
    }
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
