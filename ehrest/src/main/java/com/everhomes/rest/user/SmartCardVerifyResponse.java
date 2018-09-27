package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

public class SmartCardVerifyResponse {
    private Long verifyOk;

    public Long getVerifyOk() {
        return verifyOk;
    }

    public void setVerifyOk(Long verifyOk) {
        this.verifyOk = verifyOk;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
