// @formatter:off
package com.everhomes.sms;

public interface SmsTemplateCode {
    static final String SCOPE = "sms.default";

    static final String YZX_SUFFIX = "yzx";
    static final String SCOPE_YZX = SCOPE + "." + YZX_SUFFIX;
    
    static final String KEY_VCODE = "vcode";
    
    static final int VERIFICATION_CODE = 1; // 验证码
}
