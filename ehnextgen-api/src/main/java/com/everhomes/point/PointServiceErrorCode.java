package com.everhomes.point;

/**
 * Created by xq.tian on 2017/12/4.
 */
public interface PointServiceErrorCode {

    String SCOPE = "point";

    int ERROR_POINT_SYSTEM_NOT_EXIST_CODE = 10000;// 积分系统不存在
    int ERROR_POINT_GOOD_NOT_EXIST_CODE = 10001;
    int ERROR_POINT_TUTORIAL_NOT_EXIST_CODE = 10002;// 积分指南不存在
    int ERROR_POINT_RULE_NOT_EXIST_CODE = 10003;
    int ERROR_POINT_BANNER_NOT_EXIST_CODE = 10004; // banner不存在
    int ERROR_POINT_BIZ_CALL_ERROR_CODE = 10005;// 商品获取失败

    int ERROR_POINT_CAPTCHA_VERIFY_FAILURE_CODE = 10006;// 验证码错误

    int ERROR_POINT_SYSTEM_NAME_EXIST_CODE = 10007;// 积分系统名称已存在
    int ERROR_POINT_NAME_EXIST_CODE = 10008;// 积分名称已存在

}
