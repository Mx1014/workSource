// @formatter:off
package com.everhomes.rest.statistics.event;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>startDate: 开始日期</li>
 *     <li>endDate: 结束日期</li>
 * </ul>
 */
public class StatExecuteEventTaskCommand {

    private String startDate;
    private String endDate;
    private Byte deleteOld;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Byte getDeleteOld() {
        return deleteOld;
    }

    public void setDeleteOld(Byte deleteOld) {
        this.deleteOld = deleteOld;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}