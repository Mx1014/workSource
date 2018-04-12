package com.everhomes.rest.energy;

/**
 * <ul>
 *     <li>taskId: 任务id</li>
 *     <li>errorCode：错误码 成功为200；不成功为500</li>
 *     <li>errorDescription：错误描述</li>
 * </ul>
 * Created by ying.xiong on 2018/1/6.
 */
public class ReadTaskMeterOfflineResultLog {

    private Long taskId;

    private Integer errorCode;

    private String errorDescription;

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}
