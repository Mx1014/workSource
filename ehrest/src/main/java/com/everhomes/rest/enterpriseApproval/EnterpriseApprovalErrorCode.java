package com.everhomes.rest.enterpriseApproval;

public interface EnterpriseApprovalErrorCode {

    String SCOPE = "enterpriseApproval.error";

    int ERROR_FORM_NOT_FOUND = 10001;   //  表单未找到(客户端使用)

    int ERROR_APPROVAL_GROUP_NOT_EXIST = 10005; //  审批分类不存在

    int ERROR_DUPLICATE_NAME = 10002; //  重复的名称

    int ERROR_APPROVAL_TEMPLATE_NOT_EXIST = 10003;    //  审批模板不存在

    int ERROR_DISABLE_APPROVAL_FLOW = 10004;    //  工作流未启用

    int ERROR_DISABLE_APPROVAL_FORM = 10005;    //  表单未设置

    int ERROR_NO_OUTERS = 10006;    //  没有转移目标


    int ERROR_APPROVAL_NOT_RUNNING = 20001;     //  此审批未启用，请联系管理员。

    int ERROR_APPROVAL_NO_ACCESS = 20002;       //  你不在可见范围，请联系管理员。

    int ERROR_APPROVAL_DELETE_UN_ABLE = 30001;   // 组织人事的审批暂不支持删除

}
