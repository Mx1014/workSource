// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 *<li>logoUrl : (选填)logo的url地址</li>
 *<li>logoUri : (选填)logo的uri地址</li>
 *<li>ipadThemeRgb : (选填)主题色</li>
 *<li>qrcode : (选填)二维码字符串</li>
 *<li>visitorInfoDTO : (必填)访客信息， {@link com.everhomes.rest.visitorsys.BaseVisitorInfoDTO}</li>
 *<li>officeLocationDTO : (必填)地址信息， {@link com.everhomes.rest.visitorsys.BaseOfficeLocationDTO}</li>
 * <li>guideInfo: (选填)指引信息</li>
 * <li>baseConfig: (选填)基本配置（配置中所有的是否配置和门禁配置），{@link com.everhomes.rest.visitorsys.VisitorsysBaseConfig}</li>
 *</ul>
 */
public class GetInvitationLetterForWebResponse {
    private String logoUrl;
    private String logoUri;
    private String ipadThemeRgb;
    private String qrcode;
    private String qrcodeUrl;

    private BaseVisitorInfoDTO visitorInfoDTO;
    private BaseOfficeLocationDTO officeLocationDTO;
    private VisitorsysBaseConfig baseConfig;
    private String guideInfo;

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
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

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getQrcodeUrl() {
        return qrcodeUrl;
    }

    public void setQrcodeUrl(String qrcodeUrl) {
        this.qrcodeUrl = qrcodeUrl;
    }

    public BaseVisitorInfoDTO getVisitorInfoDTO() {
        return visitorInfoDTO;
    }

    public void setVisitorInfoDTO(BaseVisitorInfoDTO visitorInfoDTO) {
        this.visitorInfoDTO = visitorInfoDTO;
    }

    public BaseOfficeLocationDTO getOfficeLocationDTO() {
        return officeLocationDTO;
    }

    public void setOfficeLocationDTO(BaseOfficeLocationDTO officeLocationDTO) {
        this.officeLocationDTO = officeLocationDTO;
    }

    public VisitorsysBaseConfig getBaseConfig() {
        return baseConfig;
    }

    public void setBaseConfig(VisitorsysBaseConfig baseConfig) {
        this.baseConfig = baseConfig;
    }

    public String getGuideInfo() {
        return guideInfo;
    }

    public void setGuideInfo(String guideInfo) {
        this.guideInfo = guideInfo;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
