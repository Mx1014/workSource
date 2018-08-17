package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
 * <p>手动初始化考勤每日统计</p>
 * <ul>
 * <li>initialDateTime: 需要初始化的日期时间戳</li>
 * </ul>
 */
public class PunchDayLogInitializeCommand {
    private Long initialDateTime;

    public Long getInitialDateTime() {
        return initialDateTime;
    }

    public void setInitialDateTime(Long initialDateTime) {
        this.initialDateTime = initialDateTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
