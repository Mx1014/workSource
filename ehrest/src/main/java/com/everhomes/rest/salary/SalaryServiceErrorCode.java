// @formatter:off
package com.everhomes.rest.salary;

public class SalaryServiceErrorCode {
    public static final String SCOPE = "salarygroup";

    public static final int ERROR_FILE_IS_EMPTY =  100001;   //   没有文件

    public static final int ERROR_CONTACTNAME_ISNULL =  100002;   //   姓名为空

    public static final int ERROR_CONTACTTOKEN_ISNULL =  100003;   //   手机号码不存在

    public static final int ERROR_SHIFA_ISNULL =  100004;   //   没有实发工资

    public static final int ERROR_USER_IS_WRONG = 100005;   //   人员不属于本次核算范围

}
