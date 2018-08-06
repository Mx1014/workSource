package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 总公司ID,必填</li>
 * <li>userId: 用户uid，空值时默认当前用户</li>
 * <li>punchExceptionRequestStatisticsItemType: 查询的考勤异常申请统计类型，参考{@link com.everhomes.rest.techpark.punch.PunchExceptionRequestStatisticsItemType}</li>
 * <li>itemNum: 统计数目</li>
 * <li>statisticsMonth: 查询的统计月份，格式yyyyMM</li>
 * </ul>
 */
public class ListPunchExceptionRequestItemDetailCommand {
    private Long organizationId;
    private Long userId;
    private Byte punchExceptionRequestStatisticsItemType;
    private Integer itemNum;
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

    public Byte getPunchExceptionRequestStatisticsItemType() {
        return punchExceptionRequestStatisticsItemType;
    }

    public void setPunchExceptionRequestStatisticsItemType(Byte punchExceptionRequestStatisticsItemType) {
        this.punchExceptionRequestStatisticsItemType = punchExceptionRequestStatisticsItemType;
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

    public Integer getItemNum() {
        return itemNum;
    }

    public void setItemNum(Integer itemNum) {
        this.itemNum = itemNum;
    }
}
