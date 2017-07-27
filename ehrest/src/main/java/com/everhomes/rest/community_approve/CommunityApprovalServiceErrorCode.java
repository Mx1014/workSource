package com.everhomes.rest.community_approve;

/**
 * Created by zhengsiting on 2017/7/27.
 */
public interface CommunityApprovalServiceErrorCode {
    static final String SCOPE = "general_approval";

    static final int ERROR_FORM_NOTFOUND = 10001;  //查询年份出错
    static final int ERROR_FORMULA_CHECK  = 10002;  //公式校验错误
}
