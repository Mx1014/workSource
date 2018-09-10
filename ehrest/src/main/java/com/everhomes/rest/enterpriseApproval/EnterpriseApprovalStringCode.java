package com.everhomes.rest.enterpriseApproval;

public interface EnterpriseApprovalStringCode {
    String SCOPE = "enterpriseApproval";

    String APPLIER = "10001";          //  申请人
    String APPROVAL_NUMBER = "10002";       //  审批编号
    String APPROVAL_CREATE_TIME = "10003";  //  申请时间
    String APPLIER_DEPARTMENT = "10004";    //  所在部门
    String APPLIER_JOB_POSITION = "10005";  //  所在岗位

    String START_TIME = "10011";    //  开始时间
    String END_TIME = "10012";      //  结束时间

    String ASK_FOR_LEAVE_TYPE = "20001";    //  请假类型
    String ASK_FOR_LEAVE_TIME = "20002";    //  请假时长

    String BUSINESS_TIME = "20011"; //  出差时长

    String OVERTIME_TIME = "20021"; //  加班时长

    String GO_OUT_TIME = "20031";   //  外出时长

    String ABNORMAL_PUNCH_DATE = "20041";   //  异常日期
    String ABNORMAL_PUNCH_CLASS = "20042";  //  异常班次

    String CHECK_IN_TIME = "20051";     //  入职日期
    String EMPLOY_TIME = "20052";       //  申请转正日期
    String EMPLOY_REASON = "20053";     //  申请转正理由

    String DISMISS_TIME = "20061";      //  申请离职日期
    String DISMISS_REASON = "20062";   //  离职原因
    String DISMISS_REMARK = "20063";   //  离职原因备注
}
