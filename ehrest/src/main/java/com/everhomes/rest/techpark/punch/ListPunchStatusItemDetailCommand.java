package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 总公司ID,必填</li>
 * <li>userId: 用户uid，空值时默认当前用户</li>
 * <li>punchStatusStatisticsItemType: 查询的出勤统计类型，参考{@link com.everhomes.rest.techpark.punch.PunchStatusStatisticsItemType}</li>
 * <li>statisticsMonth: 查询的统计月份，格式yyyyMM</li>
 * </ul>
 */
public class ListPunchStatusItemDetailCommand {
    private Long organizationId;
    private Long userId;
    private Byte punchStatusStatisticsItemType;
    private String statisticsMonth;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Byte getPunchStatusStatisticsItemType() {
        return punchStatusStatisticsItemType;
    }

    public void setPunchStatusStatisticsItemType(Byte punchStatusStatisticsItemType) {
        this.punchStatusStatisticsItemType = punchStatusStatisticsItemType;
    }

    public String getStatisticsMonth() {
        return statisticsMonth;
    }

    public void setStatisticsMonth(String statisticsMonth) {
        this.statisticsMonth = statisticsMonth;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
