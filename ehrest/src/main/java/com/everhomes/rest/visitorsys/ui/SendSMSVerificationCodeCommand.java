// @formatter:off
package com.everhomes.rest.visitorsys.ui;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>appkey: (必填)appkey</li>
 * <li>signature: (必填)签名</li>
 * <li>nonce: (必填)3位随机数</li>
 * <li>timestamp: (必填)当前时间戳</li>
 * <li>deviceType: (必填)设备类型，{@link com.everhomes.rest.visitorsys.VisitorsysDeviceType}</li>
 * <li>deviceId: (必填)设备唯一标识</li>
 * <li>visitorPhone: (必填)电话号码</li>
 * </ul>
 */
public class SendSMSVerificationCodeCommand extends BaseVisitorsysUICommand{
    private String visitorPhone;

    public String getVisitorPhone() {
        return visitorPhone;
    }

    public void setVisitorPhone(String visitorPhone) {
        this.visitorPhone = visitorPhone;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
