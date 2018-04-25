package com.everhomes.rest.acl;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>id: 来自list接口中的profileId。如果没有的话不传，但是要传下面一个参数originId</li>
 *     <li>originId: 应用originId</li>
 *     <li>description: 应用描述</li>
 *     <li>mobileFlag: 支持移动端（0：不支持，1：支持）</li>
 *     <li>mobileUri: 移动端图片</li>
 *     <li>pcFlag: 支持PC端（0：不支持，1：支持）</li>
 *     <li>pcUri: PC端图片</li>
 *     <li>appEntryInfos: 应用入口信息, 参考{@link AppEntryInfoDTO}</li>
 *     <li>independentConfigFlag: 允许独立配置</li>
 *     <li>configAppIds: 若不支持独立配置，选择同时需要配置的应用，此处Id为originId</li>
 *     <li>supportThirdFlag: 支持对接硬件和第三方系统</li>
 *     <li>defaultFlag: 新建企业默认安装</li>
 * </ul>
 */
public class UpdateAppProfileCommand {
    private Long id;
    private Long originId;
    private String description;
    private Integer mobileFlag;
    private List<String> mobileUris;
    private Integer pcFlag;
    private List<String> pcUris;
    private List<AppEntryInfoDTO> appEntryInfos;
    private Integer independentConfigFlag;
    private List<Long> configAppIds;
    private Integer supportThirdFlag;
    private Integer defaultFlag;

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

    public Integer getMobileFlag() {
        return mobileFlag;
    }

    public void setMobileFlag(Integer mobileFlag) {
        this.mobileFlag = mobileFlag;
    }

    public List<String> getMobileUris() {
        return mobileUris;
    }

    public void setMobileUris(List<String> mobileUris) {
        this.mobileUris = mobileUris;
    }

    public Integer getPcFlag() {
        return pcFlag;
    }

    public void setPcFlag(Integer pcFlag) {
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

    public Integer getIndependentConfigFlag() {
        return independentConfigFlag;
    }

    public void setIndependentConfigFlag(Integer independentConfigFlag) {
        this.independentConfigFlag = independentConfigFlag;
    }

    public List<Long> getConfigAppIds() {
        return configAppIds;
    }

    public void setConfigAppIds(List<Long> configAppIds) {
        this.configAppIds = configAppIds;
    }

    public Integer getSupportThirdFlag() {
        return supportThirdFlag;
    }

    public void setSupportThirdFlag(Integer supportThirdFlag) {
        this.supportThirdFlag = supportThirdFlag;
    }

    public Integer getDefaultFlag() {
        return defaultFlag;
    }

    public void setDefaultFlag(Integer defaultFlag) {
        this.defaultFlag = defaultFlag;
    }
}
