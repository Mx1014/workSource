package com.everhomes.rest.archives;

public interface ArchivesLocaleStringCode {

    String SCOPE = "archives";

    int ERROR_NAME_IS_EMPTY = 100001;    //  姓名不能为空
    int ERROR_NAME_TOO_LONG = 100002;    //  姓名过长
    int ERROR_NAME_WRONG_FORMAT = 100003;    //  姓名格式不对
    int ERROR_CONTACT_TOKEN_IS_EMPTY = 100004;    //  手机号不能为空
    int ERROR_CONTACT_TOKEN_WRONG_FORMAT = 100005;  //  手机号格式错误
    int ERROR_CHECK_IN_TIME_IS_EMPTY = 100006;  //  入职时间不能为空
    int ERROR_EMPLOYEE_TYPE_IS_EMPTY = 100007;  //  员工类型不能为空
    int ERROR_DEPARTMENT_NOT_FOUND = 100008; //  部门不存在
    int ERROR_JOB_POSITION_NOT_FOUND = 100009; //  职务不存在
    int ERROR_CONTACT_EN_NAME_WRONG_FORMAT = 100010;  //  英文名格式错误
    int ERROR_WORK_EMAIL_WRONG_FORMAT = 100011;  //  邮箱格式错误
    int ERROR_CONTACT_SHORT_TOKEN_WRONG_FORMAT = 100012;  //  短号格式错误
    int ERROR_DATE_WRONG_FORMAT = 100013;   //  日期格式错误
    int ERROR_DUPLICATE_WORK_EMAIL = 100014; //  邮箱重复
    int ERROR_DUPLICATE_ACCOUNT = 100015;   //  账号重复
    int ERROR_ACCOUNT_WRONG_FORMAT = 100016;    //  账号长度不对或格式错误
    int ERROR_ACCOUNT_UPDATE_REFUSE = 100017;   //  账号不能修改

    int ERROR_NO_TARGETS = 200001;  //  通知目标为空
    int ERROR_DELETE_ADMIN = 200002;    //  无法删除管理员，请先解除其管理权限再重试

    String CONTACT_DELETE = "1001";  //  通讯录移除
    String CONTACT_LIST = "1002";    //  通讯录成员列表
    String EMPLOYEE_IMPORT_MODULE = "1003";  //  人员档案导入模板
    String EMPLOYEE_LIST = "1004";   //  人员档案列表
    String EMPLOYEE_IMPORT_REMARK = "1005";  //  人员档案导入备注
}
