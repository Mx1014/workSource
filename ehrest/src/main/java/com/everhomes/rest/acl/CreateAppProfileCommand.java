package com.everhomes.rest.acl;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>originId：关联应用id</li>
 * <li>appCategory：应用分类信息</li>
 * <li>version: 版本号</li>
 * <li>description: 应用描述</li>
 * <li>mobile_flag: 支持移动端（0：不支持，1：支持）</li>
 * <li>mobile_uri: 移动端图片</li>
 * <li>pc_flag: 支持PC端（0：不支持，1：支持）</li>
 * <li>pc_uri: PC端图片</li>
 * <li>app_entry_infos: 应用入口信息, 参考{@link AppEntryInfoDTO}</li>
 * <li>independent_config_flag: 允许独立配置</li>
 * <li>dependent_appIds: 若不支持独立配置，选择同时需要配置的应用</li>
 * <li>support_third_flag: 支持对接硬件和第三方系统</li>
 * <li>default_flag: 新建企业默认安装</li>
 * </ul>
 */
public class CreateAppProfileCommand {
    private Long originId;
    private String appCategory;
    private String version;
    private String description;
    private Integer mobile_flag;
    private List<String> mobile_uri;
    private Integer pc_flag;
    private List<String> pc_uri;
    private List<AppEntryInfoDTO> app_entry_infos;
    private Integer independent_config_flag;
    private List<Long> dependent_appIds;
    private Integer support_third_flag;
    private Integer default_flag;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


    public String getAppCategory() {
        return appCategory;
    }

    public void setAppCategory(String appCategory) {
        this.appCategory = appCategory;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMobile_flag() {
        return mobile_flag;
    }

    public void setMobile_flag(Integer mobile_flag) {
        this.mobile_flag = mobile_flag;
    }

    public List<String> getMobile_uri() {
        return mobile_uri;
    }

    public void setMobile_uri(List<String> mobile_uri) {
        this.mobile_uri = mobile_uri;
    }

    public Integer getPc_flag() {
        return pc_flag;
    }

    public void setPc_flag(Integer pc_flag) {
        this.pc_flag = pc_flag;
    }

    public List<String> getPc_uri() {
        return pc_uri;
    }

    public void setPc_uri(List<String> pc_uri) {
        this.pc_uri = pc_uri;
    }


    public Integer getIndependent_config_flag() {
        return independent_config_flag;
    }

    public void setIndependent_config_flag(Integer independent_config_flag) {
        this.independent_config_flag = independent_config_flag;
    }

    public Integer getSupport_third_flag() {
        return support_third_flag;
    }

    public void setSupport_third_flag(Integer support_third_flag) {
        this.support_third_flag = support_third_flag;
    }

    public Integer getDefault_flag() {
        return default_flag;
    }

    public void setDefault_flag(Integer default_flag) {
        this.default_flag = default_flag;
    }

    public List<Long> getDependent_appIds() {
        return dependent_appIds;
    }

    public void setDependent_appIds(List<Long> dependent_appIds) {
        this.dependent_appIds = dependent_appIds;
    }

    public Long getOriginId() {
        return originId;
    }

    public void setOriginId(Long originId) {
        this.originId = originId;
    }

    public List<AppEntryInfoDTO> getApp_entry_infos() {
        return app_entry_infos;
    }

    public void setApp_entry_infos(List<AppEntryInfoDTO> app_entry_infos) {
        this.app_entry_infos = app_entry_infos;
    }
}
