package com.everhomes.rest.uniongroup;

/**
 * Created by lei.lv on 2017/6/29.
 */
public interface UniongroupErrorCode {
    String SCOPE = "uniongroup";

    int ERROR_NO_PRIVILEGED = 100001; //没有权限

    int ERROR_INVALID_PARAMETER = 100011;//无效参数

    int ERROR_ORG_TASK_NOT_EXIST = 100201; //任务不存在
}
