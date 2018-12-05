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
    static final int ERROR_INVALID_ACTIVITY_SIGNUP_END_TIME=10018;
    static final int ERROR_BEYOND_ACTIVITY_SIGNUP_END_TIME=10019;
	static final int ERROR_CONVERT_TO_COMMON_ORDER_FAIL = 10022;
	static final int ERROR_PAYAMOUNT_ERROR = 10023;
	static final int ERROR_ORDERTYPE_NO_FIND = 10024;
	static final int ERROR_CANCEL_BEYOND_SIGNUP_TIME = 10025;
	static final int ERROR_CREATE_WXJS_ORDER_ERROR = 10026;
    static final int ERROR_VERSION_NOT_SUPPORT_PAY = 10027;
    static final int ERROR_INVALID_REALNAME = 10028;
    static final int ERROR_PHONE=10029;
	static final int ERROR_NO_ROSTER = 10030;
	static final int ERROR_ROSTER_ALREADY_EXIST = 10031;
	static final int ERROR_ACTIVITY_END = 10032;// 活动已经结束
    static final int ERROR_USER_NOT_LOGIN = 10033; //用户未登录

	static final int ERROR_URL_NOTEXIST = 10501;//URL未配置
}
