package com.everhomes.point;

/**
 * Created by xq.tian on 2017/12/4.
 */
public interface PointServiceErrorCode {

    String SCOPE = "point";

    int ERROR_POINT_SYSTEM_NOT_EXIST_CODE = 10000;
    int ERROR_POINT_GOOD_NOT_EXIST_CODE = 10001;
    int ERROR_POINT_TUTORIAL_NOT_EXIST_CODE = 10002;
    int ERROR_POINT_RULE_NOT_EXIST_CODE = 10003;
    int ERROR_POINT_BANNER_NOT_EXIST_CODE = 10004;
    int ERROR_POINT_BIZ_CALL_ERROR_CODE = 10005;

    int ERROR_POINT_CAPTCHA_VERIFY_FAILURE_CODE = 10006;

}
