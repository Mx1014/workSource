package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 总公司ID，必填</li>
 * <li>statisticsMonth: 查询的统计月份，格式:yyyyMM，空值时默认当月</li>
 * <li>userId: 用户uid，空值时默认当前用户</li>
 * </ul>
 */
public class PunchMonthlyStatisticsByMemberCommand {
    private Long organizationId;
    private String statisticsMonth;
    private Long userId;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getStatisticsMonth() {
        return statisticsMonth;
    }

    public void setStatisticsMonth(String statisticsMonth) {
        this.statisticsMonth = statisticsMonth;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
