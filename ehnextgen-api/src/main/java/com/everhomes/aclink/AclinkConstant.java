package com.everhomes.aclink;

public interface AclinkConstant {
    public static final String HOME_URL = "home.url";
    public static final String SMS_VISITOR_USER = "username";
    public static final String SMS_VISITOR_DOOR = "doorname";
    public static final String SMS_VISITOR_LINK = "link";
    public static final String SMS_VISITOR_ID = "id";
    public static final String ACLINK_DRIVER_TYPE = "aclink.qr_driver_type";			// 二维码驱动类型 令令 华润 左邻 左邻v2
    public static final String ACLINK_VISITOR_SHORTS = "aclink.visitor_shorts";     // 访客授权短句，比如
    public static final String ACLINK_QR_TIMEOUTS = "aclink.qr_timeout"; 				// 令令二维码有效时间
    public static final String ACLINK_VISITOR_CNT = "aclink.qr_visitor_cnt"; 			// 令令访问限制 
    public static final String ACLINK_QR_DRIVER_EXT = "aclink.qr_driver_ext"; 		// 二维码驱动拓展类型，比如给保安扫描的二维码
    public static final String ACLINK_QR_DRIVER_ZUOLIN_INNER = "aclink.qr_driver_zuolin_inner"; // 左邻内部驱动类型，比如 zuolin zuolinv2
    public static final String ACLINK_USERKEY_TIMEOUTS = "aclink.user_key_timeout";  // 用户钥匙的超时时间
    public static final String ACLINK_NEW_USER_AUTO_AUTH = "aclink.new_user_auto_auth";//注册用户自动授权，目前暂时是基于域空间配置
    public static final String ACLINK_JOIN_COMPANY_AUTO_AUTH = "aclink.join_company_auto_auth";//加入公司自动授权，目前暂时是基于域空间配置，未来应该有更好的管理界面
    public static final String ACLINK_QR_IMAGE_TIMEOUTS = "aclink.qr_image_timeout";
}
