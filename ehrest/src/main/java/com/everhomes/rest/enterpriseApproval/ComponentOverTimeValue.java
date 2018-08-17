package com.everhomes.rest.enterpriseApproval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>startTime: 开始时间 格式为:yy-MM-dd HH:mm</li>
 * <li>endTime: 结束时间 格式为:yy-MM-dd HH:mm</li>
 * <li>duration: 加班时长,单位天（旧版本）</li>
 * <li>durationInMinute: 加班时长,单位分钟（旧版本）</li>
 * </ul>
 */
public class ComponentOverTimeValue {

    private String startTime;

    private String endTime;

    private Double duration;

    private Long durationInMinute;

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

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Long getDurationInMinute() {
        return durationInMinute;
    }

    public void setDurationInMinute(Long durationInMinute) {
        this.durationInMinute = durationInMinute;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
