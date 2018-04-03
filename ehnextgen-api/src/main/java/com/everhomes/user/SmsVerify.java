package com.everhomes.user;

/**
 * 短信校验枚举
 * Created by xq.tian on 2017/2/28.
 */
public enum SmsVerify {
    ;
    enum Type {
        /**
         * 根据手机号校验验证码发送频率
         */
        PHONE,

        /**
         * 根据设备校验验证码发送频率
         */
        DEVICE
    }

    /**
     * 发送短信动作（注册/忘记密码）
     */
    enum Action {
        SINGUP,
        FORGOT_PASSWD
    }

    /**
     * 时间周期
     */
    enum Duration {
        SECOND,
        HOUR,
        DAY
    }
}
