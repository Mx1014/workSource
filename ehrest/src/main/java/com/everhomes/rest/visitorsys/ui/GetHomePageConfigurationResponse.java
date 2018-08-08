package com.everhomes.rest.visitorsys.ui;

import com.everhomes.rest.visitorsys.BaseVisitorDTO;
import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 * <li>zlLogoUri: (必填)左邻logouri</li>
 * <li>zlLogoUrl: (必填)左邻logourl</li>
 * <li>welcomeDocument: (必填)欢迎语句</li>
 * <li>buttonName: (必填)按钮名称</li>
 * <li>configVersion: (必填)版本信息</li>
 * </ul>
 */
public class GetHomePageConfigurationResponse{
    private String zlLogoUri;
    private String zlLogoUrl;
    private String welcomeDocument;
    private String buttonName;
    private Long configVersion=0L;

    public GetHomePageConfigurationResponse() {
    }

    public GetHomePageConfigurationResponse(String zlLogoUri, String zlLogoUrl, String welcomeDocument, String buttonName, Long configVersion) {
        this.zlLogoUri = zlLogoUri;
        this.zlLogoUrl = zlLogoUrl;
        this.welcomeDocument = welcomeDocument;
        this.buttonName = buttonName;
        this.configVersion = configVersion;
    }

    public String getZlLogoUri() {
        return zlLogoUri;
    }

    public void setZlLogoUri(String zlLogoUri) {
        this.zlLogoUri = zlLogoUri;
    }

    public String getZlLogoUrl() {
        return zlLogoUrl;
    }

    public void setZlLogoUrl(String zlLogoUrl) {
        this.zlLogoUrl = zlLogoUrl;
    }

    public String getWelcomeDocument() {
        return welcomeDocument;
    }

    public void setWelcomeDocument(String welcomeDocument) {
        this.welcomeDocument = welcomeDocument;
    }

    public String getButtonName() {
        return buttonName;
    }

    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }

    public Long getConfigVersion() {
        return configVersion;
    }

    public void setConfigVersion(Long configVersion) {
        this.configVersion = configVersion;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
