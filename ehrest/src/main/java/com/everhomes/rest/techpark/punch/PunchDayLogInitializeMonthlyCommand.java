package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
 * <p>手动初始化考勤每日统计</p>
 * <ul>
 * <li>initialMonth: 格式: yyyyMM</li>
 * </ul>
 */
public class PunchDayLogInitializeMonthlyCommand {
    private String initialMonth;

    public String getInitialMonth() {
        return initialMonth;
    }

    public void setInitialMonth(String initialMonth) {
        this.initialMonth = initialMonth;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
