package com.everhomes.rest.acl;

import com.everhomes.util.StringHelper;

public class ServiceModuleAppProfileDTO {
    private String     description;
    private Byte     mobileFlag;
    private Byte     independentConfigFlag;
    private String     mobileUri;
    private String     pcUri;
    private Long     originId;
    private String     configAppIds;
    private String     version;
    private Byte     defaultFlag;
    private Byte     supportThirdFlag;
    private Byte     pcFlag;
    private String     appEntryInfo;
    private Long     id;
    private String     appCategory;


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public Byte getMobileFlag() {
        return mobileFlag;
    }


    public void setMobileFlag(Byte mobileFlag) {
        this.mobileFlag = mobileFlag;
    }


    public Byte getIndependentConfigFlag() {
        return independentConfigFlag;
    }


    public void setIndependentConfigFlag(Byte independentConfigFlag) {
        this.independentConfigFlag = independentConfigFlag;
    }


    public String getMobileUri() {
        return mobileUri;
    }


    public void setMobileUri(String mobileUri) {
        this.mobileUri = mobileUri;
    }


    public String getPcUri() {
        return pcUri;
    }


    public void setPcUri(String pcUri) {
        this.pcUri = pcUri;
    }


    public Long getOriginId() {
        return originId;
    }


    public void setOriginId(Long originId) {
        this.originId = originId;
    }


    public String getConfigAppIds() {
        return configAppIds;
    }


    public void setConfigAppIds(String configAppIds) {
        this.configAppIds = configAppIds;
    }


    public String getVersion() {
        return version;
    }


    public void setVersion(String version) {
        this.version = version;
    }


    public Byte getDefaultFlag() {
        return defaultFlag;
    }


    public void setDefaultFlag(Byte defaultFlag) {
        this.defaultFlag = defaultFlag;
    }


    public Byte getSupportThirdFlag() {
        return supportThirdFlag;
    }


    public void setSupportThirdFlag(Byte supportThirdFlag) {
        this.supportThirdFlag = supportThirdFlag;
    }


    public Byte getPcFlag() {
        return pcFlag;
    }


    public void setPcFlag(Byte pcFlag) {
        this.pcFlag = pcFlag;
    }


    public String getAppEntryInfo() {
        return appEntryInfo;
    }


    public void setAppEntryInfo(String appEntryInfo) {
        this.appEntryInfo = appEntryInfo;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getAppCategory() {
        return appCategory;
    }


    public void setAppCategory(String appCategory) {
        this.appCategory = appCategory;
    }


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

