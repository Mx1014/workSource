package com.everhomes.rest.enterpriseApproval;

public interface EnterpriseApprovalTemplateCode {
    String SCOPE = "enterpriseApproval";

    String APPLIER = "10001";          //  申请人
    String APPROVAL_NUMBER = "10002";       //  审批编号
    String APPROVAL_CREATE_TIME = "10003";  //  申请时间
    String APPLIER_DEPARTMENT = "10004";    //  所在部门

    String START_TIME = "10011";    //  开始时间
    String END_TIME = "10012";      //  结束时间

    String ASK_FOR_LEAVE_TYPE = "20001";    //  请假类型
    String ASK_FOR_LEAVE_TIME = "20002";    //  请假时长
    String BUSINESS_TIME = "20011"; //  出差时长
    String OVERTIME_TIME = "20021"; //  加班时长
    String GO_OUT_TIME = "20031";   //  外出时长
    String ABNORMAL_PUNCH_DATE = "20041";   //  异常日期
    String ABNORMAL_PUNCH_CLASS = "20042";  //  异常班次

}
