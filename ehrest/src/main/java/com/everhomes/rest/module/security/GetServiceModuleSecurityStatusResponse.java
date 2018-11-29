package com.everhomes.rest.module.security;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>status: 安全防护状态，参考{@link com.everhomes.rest.module.security.ServiceModuleSecurityStatus}</li>
 * </ul>
 */
public class GetServiceModuleSecurityStatusResponse {
    private Byte status;

    public GetServiceModuleSecurityStatusResponse() {

    }

    public GetServiceModuleSecurityStatusResponse(Byte status) {
        this.status = status;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
