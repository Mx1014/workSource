// @formatter:off
package com.everhomes.rest.activity;

public interface ActivityServiceErrorCode {
    static final String SCOPE = "activity";
    static final int ERROR_INVALID_ACTIVITY_ID=10000; 
    static final int ERROR_INVALID_POST_ID=10001;
    static final int ERROR_INVILID_OPERATION=10002;
    static final int ERROR_INVALID_CONTACT_NUMBER=10003;
    static final int ERROR_INVALID_CONTACT_FAMILY=10004;
    static final int ERROR_INVALID_USER=10005;
    static final int ERROR_INVALID_ACTIVITY_ROSTER=10006;
    static final int ERROR_CHECKIN_BEFORE=10007;
    static final int ERROR_CHECKIN_UN_CONFIRMED=10008;
    static final int ERROR_CHECKIN_ALREADY=10009;
}
