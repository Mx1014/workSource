package com.everhomes.aclink;

public interface AclinkConstant {
    public static final String HOME_URL = "home.url";
    public static final String SMS_VISITOR_USER = "username";
    public static final String SMS_VISITOR_DOOR = "doorname";
    public static final String SMS_VISITOR_LINK = "link";
    public static final String SMS_VISITOR_ID = "id";
    public static final String ACLINK_DRIVER_TYPE = "aclink.qr_driver_type";
    public static final String ACLINK_VISITOR_SHORTS = "aclink.visitor_shorts";
    public static final String ACLINK_QR_TIMEOUTS = "aclink.qr_timeout";
    public static final String ACLINK_VISITOR_CNT = "aclink.qr_visitor_cnt";
    public static final String ACLINK_QR_DRIVER_EXT = "aclink.qr_driver_ext";
    public static final String ACLINK_QR_DRIVER_ZUOLIN_INNER = "aclink.qr_driver_zuolin_inner";
    public static final String ACLINK_USERKEY_TIMEOUTS = "aclink.user_key_timeout";
    public static final String ACLINK_NEW_USER_AUTO_AUTH = "aclink.new_user_auto_auth";//注册用户自动授权，目前暂时是基于域空间配置
    public static final String ACLINK_JOIN_COMPANY_AUTO_AUTH = "aclink.join_company_auto_auth";//加入公司自动授权，目前暂时是基于域空间配置，未来应该有更好的管理界面
    public static final String ACLINK_QR_IMAGE_TIMEOUTS = "aclink.qr_image_timeout";
}
