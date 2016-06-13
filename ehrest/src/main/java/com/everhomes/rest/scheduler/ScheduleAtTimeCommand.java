// @formatter:off
package com.everhomes.rest.scheduler;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>triggerName: 触发器名称</li>
 * <li>jobName: job名称</li>
 * <li>startTime: 开始时间，格式：yyyy-MM-dd HH:mm:ss</li>
 * <li>jobClassName: job类路径</li>
 * <li>parameterJson: 参数字符串，JSON格式</li>
 * </ul>
 */
public class ScheduleAtTimeCommand {
    private String triggerName;
    private String jobName;
    private String startTime;
    private String jobClassName;
    private String parameterJson;
    
    public ScheduleAtTimeCommand() {
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getJobClassName() {
        return jobClassName;
    }

    public void setJobClassName(String jobClassName) {
        this.jobClassName = jobClassName;
    }

    public String getParameterJson() {
        return parameterJson;
    }

    public void setParameterJson(String parameterJson) {
        this.parameterJson = parameterJson;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
