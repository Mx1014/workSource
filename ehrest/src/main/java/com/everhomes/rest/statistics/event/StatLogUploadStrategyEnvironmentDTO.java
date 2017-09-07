// @formatter:off
package com.everhomes.rest.statistics.event;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>access: WIFI, GSM {@link com.everhomes.rest.statistics.event.StatEventLogUploadStrategyAccess}</li>
 *     <li>strategy: 上传策略 {@link com.everhomes.rest.statistics.event.StatLogUploadStrategy}</li>
 *     <li>intervalSeconds: 如果是定时上传,则是间隔时间,单位:秒</li>
 *     <li>timesPerDay: 每天上传次数</li>
 * </ul>
 */
public class StatLogUploadStrategyEnvironmentDTO {

    private String access;
    private Byte strategy;
    private Integer intervalSeconds;
    private Integer timesPerDay;

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public Byte getStrategy() {
        return strategy;
    }

    public void setStrategy(Byte strategy) {
        this.strategy = strategy;
    }

    public Integer getIntervalSeconds() {
        return intervalSeconds;
    }

    public void setIntervalSeconds(Integer intervalSeconds) {
        this.intervalSeconds = intervalSeconds;
    }

    public Integer getTimesPerDay() {
        return timesPerDay;
    }

    public void setTimesPerDay(Integer timesPerDay) {
        this.timesPerDay = timesPerDay;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}