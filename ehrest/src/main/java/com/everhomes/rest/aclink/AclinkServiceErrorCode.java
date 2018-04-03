package com.everhomes.rest.aclink;

public interface AclinkServiceErrorCode {
    static final String SCOPE = "aclink";
    
    static final int ERROR_ACLINK_DOOR_EXISTS = 10001;
    static final int ERROR_ACLINK_ACTIVING_FAILED = 10002;
    static final int ERROR_ACLINK_DOOR_NOT_FOUND = 10003;
    static final int ERROR_ACLINK_STATE_ERROR = 10004;
    static final int ERROR_ACLINK_PARAM_ERROR = 10005;
    static final int ERROR_ACLINK_USER_NOT_FOUND = 10006;
    static final int ERROR_ACLINK_USER_AUTH_ERROR = 10007;
    static final int ERROR_ACLINK_HUARUN_ERROR = 10008;
}
