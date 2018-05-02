// @formatter:off
package com.everhomes.rest.visitorsys.ui;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>deviceType: (必填)设备类型，{@link com.everhomes.rest.visitorsys.VisitorsysDeviceType}</li>
 * <li>deviceId: (必填)设备唯一标识</li>
 * <li>verificationCode: (必填)验证码</li>
 * </ul>
 */
public class ConfirmVerificationCodeCommand extends BaseVisitorsysUICommand{
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
