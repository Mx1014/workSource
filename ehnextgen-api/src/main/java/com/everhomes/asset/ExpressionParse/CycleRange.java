package com.everhomes.asset.ExpressionParse;

import java.util.Date;

/**
 * 描述一个账单周期的开始和结束时间
 */
public class CycleRange {

    private Date startTime;

    private Date stopTime;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }
}
