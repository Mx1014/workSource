package com.everhomes.rest.acl;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>id: 来自list接口中的profileId，无则不传</li>
 *     <li>originId: 应用originId</li>
 *     <li>appNo: 编号</li>
 *     <li>displayVersion: 版本</li>
 *     <li>description: 应用描述</li>
 *     <li>mobileFlag: 支持移动端（0：不支持，1：支持）</li>
 *     <li>mobileUris: mobileUris</li>
 *     <li>pcFlag: 支持PC端（0：不支持，1：支持）</li>
 *     <li>pcUris: pcUris</li>
 *     <li>appEntryInfos: 应用入口信息, 参考{@link AppEntryInfoDTO}</li>
 *     <li>independentConfigFlag: 允许独立配置</li>
 *     <li>dependentAppIds: dependentAppIds</li>
 *     <li>supportThirdFlag: 支持对接硬件和第三方系统</li>
 *     <li>defaultAppFlag: 新建企业默认安装</li>
 *     <li>iconUri: iconUri</li>
 * </ul>
 */
public class UpdateAppProfileCommand {
    private Long id;
    private Long originId;
    private String appNo;
    private String displayVersion;
    private String description;
    private Byte mobileFlag;
    private List<String> mobileUris;
    private Byte pcFlag;
    private List<String> pcUris;
    private List<AppEntryInfoDTO> appEntryInfos;
    private Byte independentConfigFlag;
    private List<Long> dependentAppIds;
    private Byte supportThirdFlag;
    private Byte defaultAppFlag;
    private String iconUri;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOriginId() {
        return originId;
    }

    public void setOriginId(Long originId) {
        this.originId = originId;
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

    public List<String> getMobileUris() {
        return mobileUris;
    }

    public void setMobileUris(List<String> mobileUris) {
        this.mobileUris = mobileUris;
    }

    public Byte getPcFlag() {
        return pcFlag;
    }

    public void setPcFlag(Byte pcFlag) {
        this.pcFlag = pcFlag;
    }

    public List<String> getPcUris() {
        return pcUris;
    }

    public void setPcUris(List<String> pcUris) {
        this.pcUris = pcUris;
    }

    public List<AppEntryInfoDTO> getAppEntryInfos() {
        return appEntryInfos;
    }

    public void setAppEntryInfos(List<AppEntryInfoDTO> appEntryInfos) {
        this.appEntryInfos = appEntryInfos;
    }

    public Byte getIndependentConfigFlag() {
        return independentConfigFlag;
    }

    public void setIndependentConfigFlag(Byte independentConfigFlag) {
        this.independentConfigFlag = independentConfigFlag;
    }

    public List<Long> getDependentAppIds() {
        return dependentAppIds;
    }

    public void setDependentAppIds(List<Long> dependentAppIds) {
        this.dependentAppIds = dependentAppIds;
    }

    public Byte getSupportThirdFlag() {
        return supportThirdFlag;
    }

    public void setSupportThirdFlag(Byte supportThirdFlag) {
        this.supportThirdFlag = supportThirdFlag;
    }

    public Byte getDefaultAppFlag() {
        return defaultAppFlag;
    }

    public void setDefaultAppFlag(Byte defaultAppFlag) {
        this.defaultAppFlag = defaultAppFlag;
    }

    public String getAppNo() {
        return appNo;
    }

    public void setAppNo(String appNo) {
        this.appNo = appNo;
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
}
