// @formatter:off
package com.everhomes.rest.parking.clearance;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>status: 权限状态 {@link com.everhomes.rest.parking.clearance.CheckAuthorityStatus}</li>
 * </ul>
 */
public class CheckAuthorityResponse {

    private Byte status;

    public CheckAuthorityResponse() {
    }

    public CheckAuthorityResponse(Byte status) {
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
