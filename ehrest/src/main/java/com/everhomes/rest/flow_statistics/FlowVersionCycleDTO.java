package com.everhomes.rest.flow_statistics;

import com.everhomes.util.StringHelper;

import java.sql.Date;

/**
 * <ul>
 * <li>startDate: 起始时间</li>
 * <li>endDate: 结束时间</li>
 * </ul>
 */
public class FlowVersionCycleDTO {

    private Date startDate;//起始时间
    private Date endDate ;//结束时间(需求是定义到日期)

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
