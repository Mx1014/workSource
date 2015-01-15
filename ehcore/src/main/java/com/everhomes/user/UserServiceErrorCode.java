package com.everhomes.user;

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
}
