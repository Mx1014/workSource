// @formatter:off
package com.everhomes.rest.scheduler;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>triggerName: 触发器名称</li>
 * <li>jobName: job名称</li>
 * <li>startTime: 开始时间，格式：yyyy-MM-dd HH:mm:ss</li>
 * <li>repeatInterval: 重复间隔</li>
 * <li>repeatCount: 重复次数</li>
 * <li>jobClassName: job类路径</li>
 * <li>parameterJson: 参数字符串，JSON格式</li>
 * </ul>
 */
public class ScheduleRepeatJobCommand {
    private String triggerName;
    private String jobName;
    private String startTime;
    private Long repeatInterval;
    private Integer repeatCount;
    private String jobClassName;
    private String parameterJson;
    
    public ScheduleRepeatJobCommand() {
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

    public Long getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(Long repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    public Integer getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(Integer repeatCount) {
        this.repeatCount = repeatCount;
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
