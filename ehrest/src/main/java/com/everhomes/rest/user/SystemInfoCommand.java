package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

public class SystemInfoCommand {
    private Integer namespaceId;
    private String deviceIdentifier;
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
