// @formatter:off
package com.everhomes.rest.statistics.event;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>strategy: 上传策略 {@link com.everhomes.rest.statistics.event.StatLogUploadStrategy}</li>
 *     <li>scope: 需要上传的日志范围 {@link com.everhomes.rest.statistics.event.StatLogUploadScope}</li>
 *     <li>interval: 如果是定时上传,则是间隔时间,单位:秒</li>
 *     <li>access: WIFI, GSM</li>
 * </ul>
 */
public class StatLogUploadStrategyDTO {

    private String strategy;
    private Integer scope;
    private Long interval;
    private String access;

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public Integer getScope() {
        return scope;
    }

    public void setScope(Integer scope) {
        this.scope = scope;
    }

    public Long getInterval() {
        return interval;
    }

    public void setInterval(Long interval) {
        this.interval = interval;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}