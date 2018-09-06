package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 总公司ID，必填</li>
 * <li>statisticsDate: 查询的统计日期，日期时间戳，空值时默认当天</li>
 * <li>departmentId: 查询的部门，必填</li>
 * </ul>
 */
public class PunchDailyStatisticsByDepartmentCommand {
    private Long organizationId;
    private Long statisticsDate;
    private Long departmentId;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getStatisticsDate() {
        return statisticsDate;
    }

    public void setStatisticsDate(Long statisticsDate) {
        this.statisticsDate = statisticsDate;
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
