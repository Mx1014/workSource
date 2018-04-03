// @formatter:off
package com.everhomes.rest.parking.clearance;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>status: 权限状态 {@link com.everhomes.rest.parking.clearance.CheckAuthorityStatus}</li>
 *     <li>message: 没有权限时的提示消息</li>
 * </ul>
 */
public class CheckAuthorityResponse {

    private Byte status;
    private String message;

    public CheckAuthorityResponse() {
    }

    public CheckAuthorityResponse(Byte status, String message) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
