package com.everhomes.rest.yellowPage;

public interface YellowPageServiceErrorCode {
	static final String SCOPE = "yellowPage";

    static final int ERROR_CATEGORY_IN_USE = 10001;  //有机构正在使用此分类
    static final int ERROR_CATEGORY_ALREADY_DELETED = 10002;  // category already deleted
    static final int ERROR_SERVICEALLIANCE_ALREADY_DELETED = 10003;  //ServiceAlliance already deleted
    static final int ERROR_CATEGORY_NOT_FOUNT = 10004;  //未找到上级类型
    static final int ERROR_DELETE_ROOT_CATEGORY = 10005;  //不能删除根类型
    static final int ERROR_NOTIFY_TARGET = 10006;  //推送者不存在
    static final int ERROR_NOTIFY_MOBILE_EXIST = 10007;  //该类下推送手机号已存在
    static final int ERROR_NOTIFY_TARGET_NOT_REGISTER = 10008;  //手机号未注册
    static final int ERROR_NOTIFY_EMAIL_EXIST = 10007;  //该类下推送邮箱已存在
}
