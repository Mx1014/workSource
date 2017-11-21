package com.everhomes.rest.energy;

/**
 * Created by ying.xiong on 2017/11/1.
 */
public class CalculateTaskFeeByTaskIdCommand {
    private Long taskId;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}
