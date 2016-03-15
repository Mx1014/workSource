package com.everhomes.rest.quality;

public interface QualityServiceErrorCode {

	static final String SCOPE = "quality";

    static final int ERROR_HECHA_MEMBER_EMPTY = 10001;  //业务组核查成员为空
    static final int ERROR_TASK_NOT_EXIST = 10002;  //任务为空
    static final int ERROR_STANDARD_NOT_EXIST = 10003;  //标准为空
    static final int ERROR_CATEGORY_NOT_EXIST = 10004;  //category为空
    static final int ERROR_FACTOR_NOT_EXIST = 10005;  //factor为空
}
