// @formatter:off
package com.everhomes.serviceModuleApp;

import com.everhomes.server.schema.tables.pojos.EhServiceModuleApps;
import com.everhomes.util.StringHelper;


public class ServiceModuleApp extends EhServiceModuleApps {

	private static final long serialVersionUID = -5356763594822567533L;
	
	private Long profileId;
    private Long menuId;
    private String displayVersion;
    private String appNo;
    private String description;
    private Byte mobileFlag;
    private Byte pcFlag;
    private String mobileUris;
    private String pcUris;
    private String appEntryInfos;
    private Byte independentConfigFlag;
    private String dependentAppIds;
    private Byte supportThirdFlag;
    private String iconUri;
    private Long entryId;


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

    public String getAppNo() {
        return appNo;
    }

    public void setAppNo(String appNo) {
        this.appNo = appNo;
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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getMobileUris() {
        return mobileUris;
    }

    public void setMobileUris(String mobileUris) {
        this.mobileUris = mobileUris;
    }

    public String getPcUris() {
        return pcUris;
    }

    public void setPcUris(String pcUris) {
        this.pcUris = pcUris;
    }

    public String getAppEntryInfos() {
        return appEntryInfos;
    }

    public void setAppEntryInfos(String appEntryInfos) {
        this.appEntryInfos = appEntryInfos;
    }

    public Byte getIndependentConfigFlag() {
        return independentConfigFlag;
    }

    public void setIndependentConfigFlag(Byte independentConfigFlag) {
        this.independentConfigFlag = independentConfigFlag;
    }

    public String getDependentAppIds() {
        return dependentAppIds;
    }

    public void setDependentAppIds(String dependentAppIds) {
        this.dependentAppIds = dependentAppIds;
    }

    public Byte getSupportThirdFlag() {
        return supportThirdFlag;
    }

    public void setSupportThirdFlag(Byte supportThirdFlag) {
        this.supportThirdFlag = supportThirdFlag;
    }

    public String getDisplayVersion() {
        return displayVersion;
    }

    public void setDisplayVersion(String displayVersion) {
        this.displayVersion = displayVersion;
    }

    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }

    public Long getEntryId() {
        return entryId;
    }

    public void setEntryId(Long entryId) {
        this.entryId = entryId;
    }
}