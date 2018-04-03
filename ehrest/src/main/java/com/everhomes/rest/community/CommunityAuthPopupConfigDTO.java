// @formatter:off
package com.everhomes.rest.community;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>status: 是否开启 {@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 * </ul>
 */
public class CommunityAuthPopupConfigDTO {

    private Byte status;

    public CommunityAuthPopupConfigDTO() {  }

    public CommunityAuthPopupConfigDTO(Byte status) {
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
