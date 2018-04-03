// @formatter:off
package com.everhomes.rest.user;

public interface UserNotificationTemplateCode {
    static final String SCOPE = "user.notification";
    
    static final int USER_REGISTERD = 1; // 新用户注册
    static final int USER_REGISTER_DAYS = 2; // 用户注册天数
    static final int USER_PAYMENT_NOTICE = 3; // 用户账单生成信息推送
}
