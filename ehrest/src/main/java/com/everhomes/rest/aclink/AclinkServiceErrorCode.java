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
    static final int ERROR_ACLINK_RELATION_EXISTS = 10009;//存在关联
    static final int ERROR_ACLINK_UUID_EXISTS = 10010;//uuid已存在
    static final int ERROR_ACLINK_UUID = 10011;//uuid错误
    static final int ERROR_ACLINK_LATEST_DATA = 10012;//已经是最新数据,无需同步
    static final int ERROR_ACLINK_SERVER_NOT_FOUND = 10013;
    static final int ERROR_ACLINK_MANAGER_EXIST = 10014;//存在被授权企业
}
