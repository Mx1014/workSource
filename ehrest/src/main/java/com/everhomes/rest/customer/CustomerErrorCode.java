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
    int ERROR_CUSTOMER_PATENT_NOT_EXIST = 10004;  //客户专利不存在
    int ERROR_CUSTOMER_TRADEMARK_NOT_EXIST = 10004;  //客户商标不存在
}
