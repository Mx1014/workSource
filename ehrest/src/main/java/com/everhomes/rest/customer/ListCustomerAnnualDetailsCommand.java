package com.everhomes.rest.customer;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>customerId: 客户id</li>
 *     <li>startTime: 开始统计时间</li>
 *     <li>endTime: 结束统计时间</li>
 * </ul>
 * Created by ying.xiong on 2017/11/8.
 */
public class ListCustomerAnnualDetailsCommand {
    @NotNull
    private Long customerId;
    @NotNull
    private Long startTime;
    @NotNull
    private Long endTime;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }
}
