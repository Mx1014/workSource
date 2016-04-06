package com.everhomes.aclink;

import com.everhomes.util.StringHelper;

public class AclinkConnectingCommand {
    private String uuid;
    private String encrypt_base64;
    
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getEncrypt_base64() {
        return encrypt_base64;
    }

    public void setEncrypt_base64(String encrypt_base64) {
        this.encrypt_base64 = encrypt_base64;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
