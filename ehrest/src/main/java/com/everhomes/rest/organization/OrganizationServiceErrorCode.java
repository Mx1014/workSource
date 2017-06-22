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

    int ERROR_ORG_DEPARTMENT_NOT_EXIST = 800001; //  部门不存在
    int ERROR_ORG_POSITION_NOT_EXIST = 800002;  //  岗位不存在
    int ERROR_ORG_LEVEL_NOT_EXIST = 800003; //  职级不存在
    int ERROR_GENDER_ISNULL = 800004;   // 性别未填写
    int ERROR_DEPARTMENT_ISNULL = 800005;   // 部门未填写
    int ERROR_JOBPOSITION_ISNULL = 800006;  //  岗位未填写
    int ERROR_CHECKINTIME_ISNULL = 800007;  //  入职日期未填写
    int ERROR_EMPLOYEESTATUS_ISNULL = 800008;   //  试用期未填写
    int ERROR_EMPLOYEETIME_ISNULL = 800009; //  转正日期未填写
    int ERROR_CONTACTTOKEN_FORMAT = 800010; //  电话号码格式错误
    int ERROR_SCHOOLNAME_ISNULL =800011;    //  学校名字为空
    int ERROR_DEGREE_ISNULL = 800012;   //  学历为空
    int ERROR_MAJOR_ISNULL = 800013;    //  专业为空
    int ERROR_STARTTIME_ISNULL = 800014;    //  起始日期未填写
    int ERROR_ENDTIME_ISNULL =800015;   //  结束日期未填写
    int ERROR_ENTERPRISENAME_ISNULL = 800016;   //  企业名字为空
    int ERROR_POSITION_ISNULL = 800017; //  职位为空
    int ERROR_JOBTYPE_ISNULL =800018;   //  职位类型为空
    int ERROR_INSURANCENAME_ISNULL = 800019;    //  保险名为空
    int ERROR_INSURANCEENTERPRISE_ISNULL = 800020;  //  保险企业名为空
    int ERROR_INSURANCENUMBER_ISNULL = 800021;  //  保险号为空
    int ERROR_CONTRACTNUMBER_ISNULL = 800022;   //  合同号为空
    int ERROR_DATE_FORMAT_WRONG = 800023;   //  日期格式错误

}
