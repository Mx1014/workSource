package com.everhomes.rest.pmtask;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>taskNum: 报修编号</li>
 *     <li>remark: 处理意见</li>
 * </ul>
 * Created by ying.xiong on 2017/7/14.
 */
public class NotifyTaskResultCommand {

    @NotNull
    private String taskNum;

    private String remark;

    private String appKey;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(String taskNum) {
        this.taskNum = taskNum;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
