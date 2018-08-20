package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 总公司ID，必填</li>
 * <li>statisticsMonth: 查询的统计月份，格式:yyyyMM，空值时默认当月</li>
 * <li>departmentId: 查询的部门，必填</li>
 * </ul>
 */
public class PunchMonthlyStatisticsByDepartmentCommand {
    private Long organizationId;
    private String statisticsMonth;
    private Long departmentId;

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

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
