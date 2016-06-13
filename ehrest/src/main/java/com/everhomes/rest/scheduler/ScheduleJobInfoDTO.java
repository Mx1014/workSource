// @formatter:off
package com.everhomes.rest.scheduler;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>triggerGroupName: 触发器组名称</li>
 * <li>triggerName: 触发器名称</li>
 * <li>triggerType: 触发器类型，如周期类型、简单类型等</li>
 * <li>triggerState: 调度器当前的状态</li>
 * <li>jobGroupName: job组名称</li>
 * <li>jobName: job名称</li>
 * <li>cronExpression: 周期表达式</li>
 * <li>startTime: 开始时间</li>
 * <li>endTime: 结束时间</li>
 * <li>previousFireTime: 上一次触发时间</li>
 * <li>nextFireTime: 下一次触发时间</li>
 * <li>finalFireTime: 最后一次触发时间</li>
 * <li>misfireInstruction: 错过触发的次数</li>
 * <li>repeatInterval: 重复间隔</li>
 * <li>repeatCount: 重复次数</li>
 * </ul>
 */
public class ScheduleJobInfoDTO {
    private String triggerGroupName;
    private String triggerName;
    private String triggerType;
    private String triggerState;
    private String jobGroupName;
    private String jobName;
    private String cronExpression;
    private String startTime;
    private String endTime;
    private String previousFireTime;
    private String nextFireTime;
    private String finalFireTime;
    private Integer misfireInstruction;
    private Long repeatInterval;
    private Integer repeatCount;

    public ScheduleJobInfoDTO() {
    }
        
    public String getTriggerGroupName() {
        return triggerGroupName;
    }

    public void setTriggerGroupName(String triggerGroupName) {
        this.triggerGroupName = triggerGroupName;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }

    public String getTriggerState() {
        return triggerState;
    }

    public void setTriggerState(String triggerState) {
        this.triggerState = triggerState;
    }

    public String getJobGroupName() {
        return jobGroupName;
    }

    public void setJobGroupName(String jobGroupName) {
        this.jobGroupName = jobGroupName;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPreviousFireTime() {
        return previousFireTime;
    }

    public void setPreviousFireTime(String previousFireTime) {
        this.previousFireTime = previousFireTime;
    }

    public String getNextFireTime() {
        return nextFireTime;
    }

    public void setNextFireTime(String nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    public String getFinalFireTime() {
        return finalFireTime;
    }

    public void setFinalFireTime(String finalFireTime) {
        this.finalFireTime = finalFireTime;
    }

    public Integer getMisfireInstruction() {
        return misfireInstruction;
    }

    public void setMisfireInstruction(Integer misfireInstruction) {
        this.misfireInstruction = misfireInstruction;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
