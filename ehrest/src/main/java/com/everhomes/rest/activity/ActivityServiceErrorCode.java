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
    static final int ERROR_VIDEO_SERVER_ERROR=10010;
    static final int ERROR_VIDEO_PARAM_ERROR=10011;
    static final int ERROR_VIDEO_DEVICE_RESTART=10012;
    static final int ERROR_BEYOND_CONTRAINT_QUANTITY=10013;  //活动报名人数已满，感谢关注
    static final int ERROR_QUANTITY_MUST_GREATER_THAN_ZERO=10014;  //报名人数上限应大于0！
    static final int ERROR_QUANTITY_MUST_NOT_GREATER_THAN_10000=10015;  //报名为数上限不能大于1万！
    static final int ERROR_INVALID_ACTIVITY_ATTACHMENT_ID=10016;
    static final int ERROR_INVALID_ACTIVITY_GOODS_ID=10017;

}
