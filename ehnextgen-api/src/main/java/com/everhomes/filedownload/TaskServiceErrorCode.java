package com.everhomes.filedownload;

public interface TaskServiceErrorCode {
    static final String SCOPE = "task";

    static final int TASK_NOT_FOUND = 10001;  //任务不存在

    static final int INSUFFICIENT_PRIVILEGES = 10002;  //权限不足

    static final int TASK_ALREADY_SUCCESS = 10003;  //任务已执行完成，不可取消

    static final int TASK_CANCEL_FAIL = 10004;  //任务取消失败
}

