// @formatter:off
package com.everhomes.rest.scheduler;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>triggerName: 触发器名称</li>
 * <li>jobName: job名称</li>
 * <li>cronExpression: 周期表达式</li>
 * <li>jobClassName: job类路径</li>
 * <li>parameterJson: 参数字符串，JSON格式</li>
 * </ul>
 */
public class ScheduleCronJobCommand {
    private String triggerName;
    private String jobName;
    private String cronExpression;
    private String jobClassName;
    private String parameterJson;
    
    public ScheduleCronJobCommand() {
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

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
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
