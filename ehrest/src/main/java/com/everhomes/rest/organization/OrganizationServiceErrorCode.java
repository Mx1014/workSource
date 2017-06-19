// @formatter:off
package com.everhomes.rest.organization;

public interface OrganizationServiceErrorCode {
    static final String SCOPE = "organization";
    
    static final int ERROR_NO_PRIVILEGED = 100001; //没有权限
    
    static final int ERROR_PHONE_ALREADY_EXIST = 100010;
    
    static final int ERROR_INVALID_PARAMETER = 100011;
    
    static final int ERROR_PHONE_ALREADY_APPLY = 100012;
    
    static final int ERROR_FILE_IS_EMPTY = 100100;
    
    static final int ERROR_ORG_TASK_NOT_EXIST = 100201; //任务不存在
    
    static final int ERROR_ORG_TASK_ALREADY_PROCESSED = 100201; //任务已经被处理
    
    static final int ERROR_ORG_TASK_NOT_ASSIGNED_PERSONNEL = 100202; //未分配人员，需要指定人员
    
    static final int ERROR_ORG_TASK_CANNOT_OPERATE = 100203; //不能进行此操作
    
    static final int ERROR_FILE_CONTEXT_ISNULL=200001; //文件为空
    
    static final int ERROR_COMMUNITY_EXISTS=300001; //小区已存在
    
    static final int ERROR_ENTERPRISE_CONTACT_NOT_FOUND = 10101; //人员已不在在公司通讯录
    
    static final int ERROR_PARAMETER_NOT_EXIST = 400001;
    
    static final int ERROR_OBJECT_NOT_EXIST = 400002;
    
    static final int ERROR_ORG_EXIST = 500001; //公司已存在
    
    static final int ERROR_ORG_TYPE = 500002; //公司类型错误

    static final int ERROR_CONNOT_DELETE_ADMIN = 400003;
    
    static final int ERROR_ASSIGNMENT_EXISTS=400004; //人员角色已存在
    static final int ERROR_MOBILE_NUM = 500003; //手机号错误
    static final int ERROR_ORG_NOT_EXIST = 500004; // 公司不存在

    static final int ERROR_MEMBER_STSUTS_MODIFIED= 500005; //状态已被修改
    
    static final int ERROR_VERIFY_OVER_TIME= 600002; //认证超时

	static final int ERROR_EMAIL_NOT_EXISTS = 600001; //email 错误
	
	static final int ERROR_EMAIL_REPEAT = 600003; //email 已占用
	static final int ERROR_SEND_EMAIL = 600004; //email 发邮件错误

	static final int ERROR_ORG_JOB_POSITION_EXISTS = 600005; //通用岗位已存在

    int ERROR_CONTACTNAME_ISNULL = 700000;

    int ERROR_CONTACTTOKEN_ISNULL = 700001;

    int ERROR_GENDER_ISNULL = 700002;   // 性别未填写
    int ERROR_DEPARTMENT_ISNULL = 700003;   // 部门未填写
    int ERROR_JOBPOSITION_ISNULL = 700004;  //  岗位未填写
    int ERROR_CHECKINTIME_ISNULL = 700005;  //  入职日期未填写
    int ERROR_EMPLOYEESTATUS_ISNULL = 700006;   //  试用期未填写
    int ERROR_EMPLOYEETIME_ISNULL = 700007; //  转正日期未填写
    int ERROR_EMERGENCYCONTACT_FORMAT = 700008; //  电话号码格式错误
    int ERROR_SCHOOLNAME_ISNULL =700009;    //  学校名字为空
    int ERROR_DEGREE_ISNULL = 700010;   //  学历为空
    int ERROR_MAJOR_ISNULL = 700011;    //  专业为空
    int ERROR_STARTTIME_ISNULL = 700012;    //  起始日期未填写
    int ERROR_ENDTIME_ISNULL =700013;   //  结束日期未填写
    int ERROR_ENTERPRISENAME_ISNULL = 700014;   //  企业名字为空
    int ERROR_POSITION_ISNULL = 700015; //  职位为空
    int ERROR_JOBTYPE_ISNULL =700016;   //  职位类型为空
    int ERROR_INSURANCENAME_ISNULL = 700017;    //  保险名为空
    int ERROR_INSURANCEENTERPRISE_ISNULL = 700018;  //  保险企业名为空
    int ERROR_INSURANCENUMBER_ISNULL = 700019;  //  保险号为空
    int ERROR_CONTRACTNUMBER_ISNULL = 700020;   //  合同号为空
    int ERROR_DATE_FORMAT_WRONG = 700021;   //  日期格式错误
}
