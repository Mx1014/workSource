// @formatter:off
package com.everhomes.rest.user;

public interface UserServiceErrorCode {
    static final String SCOPE = "user";
    
    static final int ERROR_INVALID_SIGNUP_TOKEN = 10000;
    static final int ERROR_INVALID_LOGIN_TOKEN = 10001;
    static final int ERROR_OLD_PASSWORD_NOT_MATCH = 10002;
    static final int ERROR_INVALD_TOKEN_STATUS = 10003;
    static final int ERROR_INVALID_VERIFICATION_CODE = 10004;
    static final int ERROR_UNABLE_TO_LOCATE_USER = 10005;
    static final int ERROR_USER_NOT_EXIST = 10006;
    static final int ERROR_INVALID_PASSWORD = 10007;
    static final int ERROR_INVALID_MESSAGE_JSON_DATA = 10008;
    static final int ERROR_MISSING_MESSAGE_CHANNEL = 10009;
    static final int ERROR_ACCOUNT_NOT_ACTIVATED = 10010;
    static final int ERROR_IDENTIFIER_ALREADY_CLAIMED = 10011;
    static final int ERROR_ACCOUNT_NAME_ALREADY_EXISTS = 10012;
    static final int ERROR_ACCOUNT_PASSWORD_NOT_SET = 10013;

    static final int ERROR_CHECK_INVITATION_CODE=10014;
    static final int ERROR_INVITATION_CODE=100015;
    static final int ERROR_INVALID_PARAMS=100016;
    static final int ERROR_INVALID_PERMISSION=100017;
    static final int ERROR_KICKOFF_BY_OTHER=100018;
    static final int ERROR_UNAUTHENTITICATION=401;
    static final int ERROR_FORBIDDEN=403;
    static final int ERROR_FILE_CONTEXT_ISNULL=200001;

    
    static final int ERROR_INVALID_SCENE_TOKEN = 100101;

    int ERROR_SMS_MIN_DURATION = 300001;//发送验证码时间不得小于60s
    int ERROR_SMS_TOO_FREQUENT_HOUR = 300002;//验证码请求过于频繁，请1小时候重试
    int ERROR_SMS_TOO_FREQUENT_DAY = 300003;//验证码请求过于频繁，请24小时后重试

    static final int DOMAIN_NOT_CONFIGURED = 400000;

}
