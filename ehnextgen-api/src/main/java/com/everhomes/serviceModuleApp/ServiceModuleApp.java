// @formatter:off
package com.everhomes.serviceModuleApp;

import com.everhomes.server.schema.tables.pojos.EhServiceModuleApps;
import com.everhomes.util.StringHelper;


public class ServiceModuleApp extends EhServiceModuleApps {

    private static final long serialVersionUID = -4427568958167322339L;

    private Long profileId;
    private Long menuId;
    private String displayVersion;
    private String appCategory;
    private String description;
    private Byte mobileFlag;
    private Byte pcFlag;
    private String mobileUri;
    private String pcUri;
    private String appEntryInfo;
    private Byte independentConfigFlag;
    private String configAppIds;
    private Byte supportThirdFlag;
    private Byte defaultFlag;


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getAppCategory() {
        return appCategory;
    }

    public void setAppCategory(String appCategory) {
        this.appCategory = appCategory;
    }

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

    public Byte getPcFlag() {
        return pcFlag;
    }

    public void setPcFlag(Byte pcFlag) {
        this.pcFlag = pcFlag;
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

    public String getAppEntryInfo() {
        return appEntryInfo;
    }

    public void setAppEntryInfo(String appEntryInfo) {
        this.appEntryInfo = appEntryInfo;
    }

    public Byte getIndependentConfigFlag() {
        return independentConfigFlag;
    }

    public void setIndependentConfigFlag(Byte independentConfigFlag) {
        this.independentConfigFlag = independentConfigFlag;
    }

    public String getConfigAppIds() {
        return configAppIds;
    }

    public void setConfigAppIds(String configAppIds) {
        this.configAppIds = configAppIds;
    }

    public Byte getSupportThirdFlag() {
        return supportThirdFlag;
    }

    public void setSupportThirdFlag(Byte supportThirdFlag) {
        this.supportThirdFlag = supportThirdFlag;
    }

    public Byte getDefaultFlag() {
        return defaultFlag;
    }

    public void setDefaultFlag(Byte defaultFlag) {
        this.defaultFlag = defaultFlag;
    }

    public String getDisplayVersion() {
        return displayVersion;
    }

    public void setDisplayVersion(String displayVersion) {
        this.displayVersion = displayVersion;
    }
}