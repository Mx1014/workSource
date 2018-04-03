// @formatter:off
package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>verificationCode: 验证码</li>
 * </ul>
 */
public class VerifyResetIdentifierCodeCommand {

    private String verificationCode;

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
