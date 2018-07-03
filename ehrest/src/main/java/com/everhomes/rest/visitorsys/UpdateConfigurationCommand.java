// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>选填字段不填则不更新相应字段</li>
 * <li>namespaceId: (必填)域空间id</li>
 * <li>ownerType: (必填)归属的类型，{@link com.everhomes.rest.visitorsys.VisitorsysOwnerType}</li>
 * <li>ownerId: (必填)归属的ID,园区/公司的ID</li>
 * <li>configVersion: (必填)配置版本</li>
 * <li>logoUri: (选填)客户端logouri地址</li>
 * <li>guideInfo: (选填)指引信息</li>
 * <li>ipadThemeRgb: (选填)客户端主题色</li>
 * <li>selfRegisterQrcodeUri: (选填)自助登记二维码uri</li>
 * <li>welcomeShowType: (选填)欢迎页面类型，image显示图片，text显示富文本</li>
 * <li>welcomePages: (选填)欢迎富文本</li>
 * <li>welcomePicUri: (选填)欢迎图片uri</li>
 * <li>secrecyAgreement: (选填)保密协议富文本</li>
 * <li>baseConfig: (选填)基本配置（配置中所有的是否配置和门禁配置），{@link com.everhomes.rest.visitorsys.VisitorsysBaseConfig}</li>
 * <li>formConfig: (选填)表单配置，{@link com.everhomes.rest.visitorsys.VisitorsysApprovalFormItem}</li>
 * <li>passCardConfig: (选填)通行证配置，{@link com.everhomes.rest.visitorsys.VisitorsysPassCardConfig}</li>
 * </ul>
 */
public class UpdateConfigurationCommand {
    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;

    private Long configVersion=0L;
    private String logoUri;
    private String ipadThemeRgb;
    private String guideInfo;
    private String selfRegisterQrcodeUri;
    private String secrecyAgreement;
    private String welcomePages;
    private String welcomePicUri;
    private String welcomeShowType;

    private VisitorsysBaseConfig baseConfig;
//    private VisitorsysFormConfig formConfig;
    @ItemType(VisitorsysApprovalFormItem.class)
    private List<VisitorsysApprovalFormItem> formConfig;
    private VisitorsysPassCardConfig passCardConfig;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getConfigVersion() {
        return configVersion;
    }

    public void setConfigVersion(Long configVersion) {
        this.configVersion = configVersion;
    }

    public String getLogoUri() {
        return logoUri;
    }

    public void setLogoUri(String logoUri) {
        this.logoUri = logoUri;
    }

    public String getIpadThemeRgb() {
        return ipadThemeRgb;
    }

    public void setIpadThemeRgb(String ipadThemeRgb) {
        this.ipadThemeRgb = ipadThemeRgb;
    }

    public String getGuideInfo() {
        return guideInfo;
    }

    public void setGuideInfo(String guideInfo) {
        this.guideInfo = guideInfo;
    }

    public String getSelfRegisterQrcodeUri() {
        return selfRegisterQrcodeUri;
    }

    public void setSelfRegisterQrcodeUri(String selfRegisterQrcodeUri) {
        this.selfRegisterQrcodeUri = selfRegisterQrcodeUri;
    }

    public String getSecrecyAgreement() {
        return secrecyAgreement;
    }

    public void setSecrecyAgreement(String secrecyAgreement) {
        this.secrecyAgreement = secrecyAgreement;
    }

    public String getWelcomePages() {
        return welcomePages;
    }

    public void setWelcomePages(String welcomePages) {
        this.welcomePages = welcomePages;
    }

    public VisitorsysBaseConfig getBaseConfig() {
        return baseConfig;
    }

    public void setBaseConfig(VisitorsysBaseConfig baseConfig) {
        this.baseConfig = baseConfig;
    }

    public List<VisitorsysApprovalFormItem> getFormConfig() {
        return formConfig;
    }

    public void setFormConfig(List<VisitorsysApprovalFormItem> formConfig) {
        this.formConfig = formConfig;
    }

    public VisitorsysPassCardConfig getPassCardConfig() {
        return passCardConfig;
    }

    public void setPassCardConfig(VisitorsysPassCardConfig passCardConfig) {
        this.passCardConfig = passCardConfig;
    }

    public String getWelcomePicUri() {
        return welcomePicUri;
    }

    public void setWelcomePicUri(String welcomePicUri) {
        this.welcomePicUri = welcomePicUri;
    }

    public String getWelcomeShowType() {
        return welcomeShowType;
    }

    public void setWelcomeShowType(String welcomeShowType) {
        this.welcomeShowType = welcomeShowType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
