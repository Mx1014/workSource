package com.everhomes.rest.investment;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

public class StatisticTime {

    private Timestamp statisticStartTime;

    private Timestamp statisticEndTime;

    public Timestamp getStatisticStartTime() {
        return statisticStartTime;
    }

    public void setStatisticStartTime(Timestamp statisticStartTime) {
        this.statisticStartTime = statisticStartTime;
    }

    public Timestamp getStatisticEndTime() {
        return statisticEndTime;
    }

    public void setStatisticEndTime(Timestamp statisticEndTime) {
        this.statisticEndTime = statisticEndTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
