package com.everhomes.enterprisepaymentauth;

public class EnterprisePaymentAuthConstants {
    public static final String ERROR_SCOPE = "enterprise_payment_auth_error";
    public static final int ERROR_INVALID_PARAMETER = 506;  // 接口参数异常
    public static final int ERROR_EMPLOYEE_NO_FOUND = 100;    // 员工不存在
    public static final int ERROR_EMPLOYEE_DISMISSAL = 101;   // 员工已离职
    public static final int ERROR_PAYMENT_AUTH_LESS = 103;  // 鉴权失败
    public static final int ERROR_SIGNATURE_WRONG = 403;


    public static final int ENTERPRISE_PAYMENT_AUTH_CODE = 1;    // 应用已授权
    public static final int ENTERPRISE_PAYMENT_UN_AUTH_CODE = 2;   // 应用未授权
    public static final int ENTERPRISE_PAYMENT_AMOUNT_EXCEEDS_LIMIT = 3;  // 授权余额不足
    public static final int ENTERPRISE_PAYMENT_EMPLOYEE_TOTAL_EXCEEDS_LIMIT = 4;  // 应用支付总额超出(员工每月)
    public static final int ENTERPRISE_PAYMENT_ORGANIZATION_TOTAL_EXCEEDS_LIMIT = 5;  // 应用支付总额超出(企业每月)


    public static final String LOCALE = "zh_CN";
    //模板

    public static final String OPERATE_LOG_SCOPE = "enterprise_payment_auth_operate_log";
    public static final int OPERATE_LOG_MAIN = 1;
    public static final int OPERATE_LOG_SCENE = 2;
    public static final int CHANGE_LOG = 3;

    public static final String MSG_SCOPE = "enterprise_payment_auth_msg";
    public static final int UPDATE_LIMIT = 1;
    public static final int PAYMENT_AUTH_USED_MESSAGE = 2;
    
    //文字
    public static final String ENTERPRISE_PAYMENT_AUTH_SCOPE = "enterprise_payment_auth";
    /**
     * 无
     */
    public static final String NO_SCENE = "1";
    /**
     * 增加
     */
    public static final String INCREASE = "2";
    /**
     * 减少
     */
    public static final String DECREASE = "3";
    /**
     * 订单编号;使用人;使用人手机;支付场景;支付时间;支付金额
     */
    public static final String EXCEL_TITLE = "4";
    /**
     * 企业支付记录
     */
    public static final String EXCEL_HEAD1 = "5";
    /**
     * 支付时间:
     */
    public static final String EXCEL_HEAD2 = "6";

    public static final String MSG_SUBJECT_1 = "7";

    public static final String SOURCE_SCOPE = "enterprise_payment_auth_operate_source";
    public static final String SOURCE_LIMIT = "1";
}
