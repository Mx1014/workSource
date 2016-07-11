package com.everhomes.aclink.lingling;

import com.everhomes.util.StringHelper;

public class AclinkLinglingQRCode {
    private Long codeId;
    private String qrcodeKey;
    
    public Long getCodeId() {
        return codeId;
    }
    public void setCodeId(Long codeId) {
        this.codeId = codeId;
    }
    public String getQrcodeKey() {
        return qrcodeKey;
    }
    public void setQrcodeKey(String qrcodeKey) {
        this.qrcodeKey = qrcodeKey;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
