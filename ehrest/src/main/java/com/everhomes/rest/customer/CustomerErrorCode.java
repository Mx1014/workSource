package com.everhomes.rest.customer;

/**
 * Created by ying.xiong on 2017/8/15.
 */
public interface CustomerErrorCode {
    String SCOPE = "customer";

    int ERROR_CUSTOMER_NOT_EXIST = 10001;  //客户不存在
    int ERROR_CUSTOMER_TALENT_NOT_EXIST = 10002;  //客户人才不存在
    int ERROR_CUSTOMER_PROJECT_NOT_EXIST = 10003;  //客户申报项目不存在
    int ERROR_CUSTOMER_COMMERCIAL_NOT_EXIST = 10004;  //客户工商信息不存在
    int ERROR_CUSTOMER_PATENT_NOT_EXIST = 10005;  //客户专利不存在
    int ERROR_CUSTOMER_TRADEMARK_NOT_EXIST = 10006;  //客户商标不存在
    int ERROR_CUSTOMER_ECONOMIC_INDICATOR_NOT_EXIST = 10007;  //客户经济指标不存在
    int ERROR_CUSTOMER_INVESTMENT_NOT_EXIST = 10008;  //客户经济指标不存在
    int ERROR_CUSTOMER_CERTIFICATE_NOT_EXIST = 10009;  //客户证书不存在
    int ERROR_CUSTOMER_NAME_IS_NULL = 10010;  //客户名称为空
    int ERROR_CUSTOMER_CONTACT_IS_NULL = 10011;  //客户联系人为空
    int ERROR_CUSTOMER_CONTACT_MOBILE_IS_NULL = 10012;  //客户联系人手机号为空
}
