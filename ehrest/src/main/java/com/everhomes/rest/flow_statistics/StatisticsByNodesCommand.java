package com.everhomes.rest.flow_statistics;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

public class StatisticsByNodesCommand {
    private Long flowMainId ;
    private Integer flowVersion;//版本号
    private Long startDate;//起始时间（入参应为某日期的0点0分0秒的时间戳）
    private Long endDate;//结束时间（入参应为某日期的23点59分59秒的时间戳）

    public Long getFlowMainId() {
        return flowMainId;
    }

    public void setFlowMainId(Long flowMainId) {
        this.flowMainId = flowMainId;
    }

    public Integer getFlowVersion() {
        return flowVersion;
    }

    public void setFlowVersion(Integer flowVersion) {
        this.flowVersion = flowVersion;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
