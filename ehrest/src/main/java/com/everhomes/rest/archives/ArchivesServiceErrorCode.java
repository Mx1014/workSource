package com.everhomes.rest.archives;

public interface ArchivesServiceErrorCode {

    static final String SCOPE = "archives";

    int ERROR_NAME_ISEMPTY = 100001;    //  姓名不能为空
    int ERROR_NAME_TOOLONG = 100002;    //  姓名过长
    int ERROR_NAME_WRONGFORMAT = 100003;    //  姓名格式不对
    int ERROR_CONTACT_TOKEN_ISEMPTY = 100004;    //  手机号不能为空
    int ERROR_CONTACT_TOKEN_WRONGFORMAT = 100005;  //  手机号格式错误
    int ERROR_CHECK_IN_TIME_ISEMPTY = 100006;  //  入职时间不能为空
    int ERROR_EMPLOYEE_TYPE_ISEMPTY = 100007;  //  员工类型不能为空
    int ERROR_DEPARTMENT_NOT_FOUND = 100008; //  部门不存在
    int ERROR_JOB_POSITION_NOT_FOUND = 100009; //  职务不存在
}
