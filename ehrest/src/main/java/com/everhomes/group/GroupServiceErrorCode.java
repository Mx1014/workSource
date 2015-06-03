// @formatter:off
package com.everhomes.group;

public interface GroupServiceErrorCode {
    static final String SCOPE = "group";

    static final int ERROR_GROUP_NOT_FOUND = 10001;
    static final int ERROR_USER_ALREADY_IN_GROUP = 10002;
    static final int ERROR_USER_IN_JOINING_GROUP_PROCESS = 10003;
    static final int ERROR_GROUP_MEMBER_NOT_FOUND = 10004;
    static final int ERROR_GROUP_MEMBER_NOT_IN_ACCEPT_STATE = 10005;
    static final int ERROR_GROUP_INVITED_USER_NOT_FOUND = 10006;
    static final int ERROR_GROUP_INVALID_ROLE_STATUS = 10007;
    static final int ERROR_GROUP_NOT_IN_ADMIN_ROLE = 10008;
    static final int ERROR_GROUP_CREATOR_LEAVE_NOT_ALLOW = 10009;
    static final int ERROR_GROUP_CREATOR_RESIGN_NOT_ALLOW = 10010;
    static final int ERROR_GROUP_CREATOR_REVOKED_NOT_ALLOW = 10011;
    static final int ERROR_GROUP_OP_REQUEST_NOT_FOUND = 10012;
    static final int ERROR_GROUP_OP_REQUEST_NOT_IN_REQUESTING_STATE = 10013;
}
