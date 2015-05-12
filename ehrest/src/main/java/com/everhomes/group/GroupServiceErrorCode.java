// @formatter:off
package com.everhomes.group;

public interface GroupServiceErrorCode {
    static final String SCOPE = "group";

    static final int ERROR_GROUP_NOT_EXIST = 10001;
    static final int ERROR_USER_ALREADY_IN_GROUP = 10002;
    static final int ERROR_USER_IN_JOINING_GROUP_PROCESS = 10003;
    static final int ERROR_GROUP_MEMBER_NOT_EXIST = 10004;
    static final int ERROR_GROUP_MEMBER_NOT_IN_ACCEPT_STATE = 10005;
}
