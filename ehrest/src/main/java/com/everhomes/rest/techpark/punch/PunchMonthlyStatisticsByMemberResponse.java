package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * <ul>
 * <li>organizationId: 总公司ID</li>
 * <li>statisticsMonth: 统计月份,格式:yyyyMM</li>
 * <li>lastUpdateTime: 数据最近更新时间</li>
 * <li>userId: 用户id</li>
 * <li>detailId: 员工detailId</li>
 * <li>workDayCount: 出勤天数</li>
 * <li>restDayCount: 休息天数</li>
 * <li>punchStatusStatisticsList: 未到、迟到、早退等出勤统计项，参考{@link com.everhomes.rest.techpark.punch.PunchStatusStatisticsItemDTO}</li>
 * <li>exceptionRequestStatisticsList: 请假、出差等申请统计项，参考{@link com.everhomes.rest.techpark.punch.PunchExceptionRequestStatisticsItemDTO}</li>
 * </ul>
 */
public class PunchMonthlyStatisticsByMemberResponse {
    private Long organizationId;
    private String statisticsMonth;
    private Long lastUpdateTime;
    private Long userId;
    private Long detailId;
    private Integer workDayCount;
    private Integer restDayCount;
    private List<PunchStatusStatisticsItemDTO> punchStatusStatisticsList;
    private List<PunchExceptionRequestStatisticsItemDTO> exceptionRequestStatisticsList;

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

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public Integer getWorkDayCount() {
        return workDayCount;
    }

    public void setWorkDayCount(Integer workDayCount) {
        this.workDayCount = workDayCount;
    }

    public Integer getRestDayCount() {
        return restDayCount;
    }

    public void setRestDayCount(Integer restDayCount) {
        this.restDayCount = restDayCount;
    }

    public Long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public List<PunchStatusStatisticsItemDTO> getPunchStatusStatisticsList() {
        if (punchStatusStatisticsList == null) {
            punchStatusStatisticsList = new ArrayList<>();
        }
        return punchStatusStatisticsList;
    }

    public void setPunchStatusStatisticsList(List<PunchStatusStatisticsItemDTO> punchStatusStatisticsList) {
        this.punchStatusStatisticsList = punchStatusStatisticsList;
    }

    public List<PunchExceptionRequestStatisticsItemDTO> getExceptionRequestStatisticsList() {
        if (exceptionRequestStatisticsList == null) {
            exceptionRequestStatisticsList = new ArrayList<>();
        }
        return exceptionRequestStatisticsList;
    }

    public void setExceptionRequestStatisticsList(List<PunchExceptionRequestStatisticsItemDTO> exceptionRequestStatisticsList) {
        this.exceptionRequestStatisticsList = exceptionRequestStatisticsList;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
