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
	
	int ERROR_ENTERPRISE_NAME_EMPTY = 80001;
    int ERROR_BUILDING_NAME_EMPTY = 80002;
    int ERROR_APARTMENT_NAME_EMPTY = 80003;
    int ERROR_BUILDING_NOT_EXIST = 80004;
    int ERROR_APARTMENT_NOT_EXIST = 80005;
    int ERROR_APARTMENT_CHECKED_IN = 80006;
    int ERROR_NO_DATA = 800000;
	
    int ERROR_ORG_DEPARTMENT_NOT_EXIST = 900001; //  部门不存在
    int ERROR_ORG_POSITION_NOT_EXIST = 900002;  //  岗位不存在
    int ERROR_ORG_LEVEL_NOT_EXIST = 900003; //  职级不存在
    int ERROR_GENDER_ISNULL = 900004;   // 性别未填写
    int ERROR_DEPARTMENT_ISNULL = 900005;   // 部门未填写
    int ERROR_JOBPOSITION_ISNULL = 900006;  //  岗位未填写
    int ERROR_CHECKINTIME_ISNULL = 900007;  //  入职日期未填写
    int ERROR_EMPLOYEESTATUS_ISNULL = 900008;   //  试用期未填写
    int ERROR_EMPLOYEETIME_ISNULL = 900009; //  转正日期未填写
    int ERROR_CONTACTTOKEN_FORMAT = 900010; //  电话号码格式错误
    int ERROR_SCHOOLNAME_ISNULL =900011;    //  学校名字为空
    int ERROR_DEGREE_ISNULL = 900012;   //  学历为空
    int ERROR_MAJOR_ISNULL = 900013;    //  专业为空
    int ERROR_STARTTIME_ISNULL = 900014;    //  起始日期未填写
    int ERROR_ENDTIME_ISNULL =900015;   //  结束日期未填写
    int ERROR_ENTERPRISENAME_ISNULL = 900016;   //  企业名字为空
    int ERROR_POSITION_ISNULL = 900017; //  职位为空
    int ERROR_JOBTYPE_ISNULL =900018;   //  职位类型为空
    int ERROR_INSURANCENAME_ISNULL = 900019;    //  保险名为空
    int ERROR_INSURANCEENTERPRISE_ISNULL = 900020;  //  保险企业名为空
    int ERROR_INSURANCENUMBER_ISNULL = 900021;  //  保险号为空
    int ERROR_CONTRACTNUMBER_ISNULL = 900022;   //  合同号为空
    int ERROR_DATE_FORMAT_WRONG = 900023;   //  日期格式错误
    int ERROR_GENDER_FORMAT_WRONG = 900024;   //  性别仅支持"男""女"
    int ERROR_CONTACTNAME_FORMAT_WRONG = 900025;    //  姓名长度需小于16
    int ERROR_UNIFIEDSOCIALCREDITCODE_EXIST = 900026;    //  unifiedSocialCreditCode唯一性

    static final int ERROR_DEPARTMENT_EXISTS = 300002; //部门已存在
    static final int ERROR_JOB_POSITION_EXISTS = 300003; //通用岗位已存在

    int ERROR_ORG_NAME_REPEAT = 900030; //名称重复

    int ERROR_FOR_HAS_COMMUNITIES = 900031; //无法注销企业。当前企业仍存在需要管理的项目。请转移项目管理权至其它公司后再试。
    int ERROR_ADMINNAME_ISNULL = 900032; //姓名为空
    int ERROR_WORKPLACENAME_ISNULL = 900033;//办公地点名称为空
    int ERROR_COMMUNITYNAME_ISNULL = 900034;//办公地点所属项目为空
    int ERROR_PMFLAG_ISNULL = 900035;//是否属于管理公司标志为空
    int ERROR_SERVICESUPPORT_ISNULL = 900036;//是否属于服务商标志为空
    int ERROR_WORKPLATFORM_ISNULL = 900037;//是否开启工作台标志为空
    int ERROR_ORGANIZATION_NAME_OVERFLOW = 900038;//公司的名称长度超过50个汉字


    //merge 发现冲突900031
    int ERROR_USER_IS_ADMINISTRATOR = 900031; //用户为管理员，不允许退出

    int ERROR_EMAIL_IS_EXISTS = 900039; //用户认证时，邮箱已被认证。
}
