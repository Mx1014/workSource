package com.everhomes.enterprisemoment;

public class EnterpriseMomentConstants {
    public static final String ERROR_SCOPE = "enterprise_moments";
    public static final int ERROR_INVALID_PARAMETER = 506;  // 接口参数异常
    public static final int ERROR_NO_MOMENT = 10001;    // 找不到动态
    public static final int ERROR_MOMENT_DELETED = 10002;    // 动态已删除
    public static final int ERROR_MOMENT_CONTENT_BLANK = 10003;    // 动态内容和图片不能同为空
    public static final int ERROR_PRIVILEGE = 40001;    // 权限异常
    public static final int ERROR_WHEN_DELETE_COMMENT_OF_OTHERS = 40002; // 不能删除其他人的评论

    public static final String SELECTOR_TYPE_SCOP = ERROR_SCOPE;
    public static final int ALL = 1;
    public static final int PUBLISH_BY_SELF = 2;
    public static final int FAVOURITE_BY_SELF = 3;
    public static final int COMMENT_BY_SELF = 4;
    public static final String TITLE = "title";

    public static final String ENTERPRISE_DEFAULT_MOMENT = "enterprise_default_moment";
    public static final String ENTERPRISE_DEFAULT_MOMENT_CREATOR_NAME = "1";
    public static final String ENTERPRISE_DEFAULT_MOMENT_CONTEXT = "2";
    public static final String ENTERPRISE_MOMENT_MESSAGE_SHOW_IMAGE = "3";

    public static final String INIT_TAG_SCOPE = ERROR_SCOPE;//标签初始化
    public static final String INIT_TAG0 = "20001";
    public static final String INIT_TAG1 = "20002";
    public static final String INIT_TAG2 = "20003";
    public static final String INIT_TAG3 = "20004";
    public static final String INIT_TAG4 = "20005";
    public static final String INIT_TAG5 = "20006";
}
