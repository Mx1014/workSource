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
    static final int ERROR_NOTIFY_EMAIL_EXIST = 10009;  //该类下推送邮箱已存在
    static final int ERROR_COMMUNITY_NOT_CHOSEN = 10010;  //未选中项目
    static final int ERROR_SERVICE_TYPE_TO_UPDATE_NOT_FOUND= 10011;  //未找到需要更新的服务类型
    static final int ERROR_COMMENT_NOT_EXIST_OR_DELETED = 10012;  //评论不存在或已被删除
    static final int ERROR_EXCEL_EXPORT_FAILED = 10013;  //文件导出失败
    static final int ERROR_SKIP_URL_FORMAT_ERROR = 10014;  //跳转链接格式错误
    static final int ERROR_INPUT_PARAM_NOT_VALID = 10015;  //接口参数异常
    static final int ERROR_QUERY_BIZ_MODULE_FAILED = 10016;  //获取电商模块失败
    static final int ERROR_SERVICE_NOT_FOUND = 10017;  //服务不存在
    
    static final int ERROR_NEW_EVENT_APPLIER_NOT_EXIST = 11000;  //新事件申请用户不存在
    static final int ERROR_NEW_EVENT_FLOW_CASE_NOT_EXIST = 11001;  //未找到工作流信息
    static final int ERROR_ALLIANCE_PROVIDER_FUNC_NOT_OPEN = 11002;  //服务商功能并未开启
    static final int ERROR_ALLIANCE_PROVIDER_NOT_FOUND = 11003;  //该服务商不存在
    static final int ERROR_NEW_EVENT_FILE_NOT_VALID = 11004;  //上传的文件地址为空
    static final int ERROR_MAIL_FILES_SAVE_ERROR = 11005;  //邮件附件获取失败
    
    static final int ERROR_ALLIANCE_TAG_NOT_VALID = 11100;  //需要更新父筛选为空
    static final int ERROR_ALLIANCE_TAG_TYPE_NOT_VALID = 11101;  //筛选的类型不合法
    
    //埋点统计
    static final int ERROR_ALLIANCE_GET_STAT_TOOL_TYPE_IS_NULL = 11200;  //获取统计信息时type为null


    //标准版
    static final int ERROR_ALLIANCE_TYPE_NOT_VALID = 11200;  //服务联盟类型有误
    static final int ERROR_OWNER_TYPE_NOT_COMMUNITY = 11201;  //项目请求参数有误
    static final int ERROR_ALLIANCE_MAIN_CONFIG_NOT_EXIST = 11202;  //通用配置(基础)不存在
    static final int ERROR_ALLIANCE_MAIN_CATEGORY_NOT_EXIST = 11203;  //基础样式不存在
    static final int ERROR_SELF_CONFIG_NOT_ENABLE = 11204;  //请先打开自定义配置开关
    static final int ERROR_CATEGORY_NOT_EXIST = 11205;  //样式不存在
    static final int ERROR_FLOW_CASE_SERVICE_NOT_FOUND= 11206;  //未获取到服务
    
    //FAQ
    static final int ERROR_FAQ_TYPE_NOT_FOUND = 11400;  //
    static final int ERROR_FAQ_NOT_FOUND = 11401;  //
    static final int ERROR_FAQ_OPERATE_SERVICE_NOT_FOUND = 11402;  //
    static final int ERROR_FAQ_TYPE_NOT_VALID2 = 11402;  //问题
    static final int ERROR_FAQ_TYPE_NOT_VALID3 = 11403;  //问题
    static final int ERROR_FAQ_TYPE_NOT_VALID4 = 11404;  //问题
    static final int ERROR_FAQ_TYPE_NOT_VALID5 = 11405;  //问题
    
}
