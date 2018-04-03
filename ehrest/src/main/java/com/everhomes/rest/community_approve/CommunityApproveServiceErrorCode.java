package com.everhomes.rest.community_approve;

/**
 * Created by zhengsiting on 2017/7/20.
 */
public interface CommunityApproveServiceErrorCode {
    static final String SCOPE = "community_approve";

    static final int ERROR_NOT_SET_FORM = 10001;  //没有设置表单
    static final int ERROR_NOT_SET_FLOW  = 10002;  //没有设置工作流
}
