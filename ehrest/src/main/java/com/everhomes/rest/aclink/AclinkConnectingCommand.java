package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

public class AclinkConnectingCommand {
    private String uuid;
    private String encryptBase64;
    
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getEncryptBase64() {
        return encryptBase64;
    }

    public void setEncryptBase64(String encryptBase64) {
        this.encryptBase64 = encryptBase64;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
