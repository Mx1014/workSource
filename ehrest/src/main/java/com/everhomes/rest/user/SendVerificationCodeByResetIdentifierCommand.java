// @formatter:off
package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>identifier: 用户新手机号</li>
 *     <li>regionCode: regionCode</li>
 * </ul>
 */
public class SendVerificationCodeByResetIdentifierCommand {

    private String identifier;
    private Integer regionCode;

    public Integer getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(Integer regionCode) {
        this.regionCode = regionCode;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
