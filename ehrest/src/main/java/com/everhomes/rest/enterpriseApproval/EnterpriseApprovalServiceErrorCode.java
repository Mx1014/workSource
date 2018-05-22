package com.everhomes.rest.enterpriseApproval;

public interface EnterpriseApprovalServiceErrorCode {

    String SCOPE = "enterprise_approval";

    int ERROR_APPROVAL_GROUP_NOT_EXIST = 10001; //  审批分类不存在

    int ERROR_DUPLICATE_NAME = 10002; //  重复的名称

    int ERROR_APPROVAL_TEMPLATE_NOT_EXIST = 10003;    //  审批模板不存在

    int ERROR_DISABLE_APPROVAL_FLOW = 10004;    //  工作流未启用
}
