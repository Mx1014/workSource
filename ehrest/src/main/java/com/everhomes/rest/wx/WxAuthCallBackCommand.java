// @formatter:off
package com.everhomes.rest.wx;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>code: 微信传过来的code</li>
 *     <li>namespaceId: 域空间id</li>
 *     <li>appId: 应用唯一标识，在微信开放平台提交应用审核通过后获得</li>
 *     <li>secret: 应用密钥AppSecret</li>
 *     <li>deviceIdentifier:设备标识</li>
 * </ul>
 */
public class WxAuthCallBackCommand {

    @NotNull
    private String code;

    @NotNull
    private Integer namespaceId;

    private String appId;

    private String secret;

    private String deviceIdentifier;

    private String pusherIdentify;

    public String getDeviceIdentifier() {
        return deviceIdentifier;
    }

    public void setDeviceIdentifier(String deviceIdentifier) {
        this.deviceIdentifier = deviceIdentifier;
    }

    public String getPusherIdentify() {
        return pusherIdentify;
    }

    public void setPusherIdentify(String pusherIdentify) {
        this.pusherIdentify = pusherIdentify;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
