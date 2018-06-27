package com.everhomes.rest.enterpriseApproval;

public interface EnterpriseApprovalTemplateCode {

    String SCOPE = "enterpriseApproval";

    int ARCHIVES_TITLE = 1; //  已存在{申请动作}计划
    int APPROVAL_TITLE = 2;  //  已存在{申请名称}

    int ARCHIVES_CONTENT = 11;   //  原定于{生效时间}{申请动作}，继续发起申请，将作废原计划，确定继续吗？
    int APPROVAL_CONTENT = 12;   //  继续发起申请，将撤回原{申请名称}，确定继续吗？

}
