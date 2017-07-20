package com.everhomes.rest.community_approve;

/**
 * Created by zhengsiting on 2017/7/20.
 */
public interface CommunityApproveServiceErrorCode {
    static final String SCOPE = "community_approve";

    static final int ERROR_FORM_NOTFOUND = 10001;  //查询年份出错
    static final int ERROR_FORMULA_CHECK  = 10002;  //公式校验错误
}
