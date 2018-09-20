package com.everhomes.rest.user;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间，为空则用默认的</li>
 * <li>deviceIdentifier: 设备唯一 ID</li>
 * <li>pusherIdentify: iOS 使用，（1）appstore(正式版本) .iOS推送证书+使用正式推送服务器，标示appstore（2）develop的包（用电脑真机调试的包）.zuolin推送证书+用测试推送服务器，标示develop（3）ad-hoc的包（真机内测） .zuolin推送证书 + 正式推送服务器，标示appbeta。是否使用 sandbox 测试的服务器；android 使用，则表示华为或者小米的 PusherToken</li>
 * </ul>
 * @author janson
 *
 */
public class SystemInfoCommand {
    private Integer namespaceId;
    @NotNull
    private String deviceIdentifier;
    @NotNull
    private String pusherIdentify;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
