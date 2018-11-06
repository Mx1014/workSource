package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * <ul>
 * <li>statisticsMonth: 查询的统计月份,格式:yyyyMM</li>
 * <li>departmentId: 部门ID</li>
 * <li>lastUpdateTime: 数据最近更新时间</li>
 * <li>punchStatusStatisticsList: 未到、迟到、早退等出勤统计项，参考{@link com.everhomes.rest.techpark.punch.PunchStatusStatisticsItemDTO}</li>
 * <li>exceptionRequestStatisticsList: 请假、出差等申请统计项，参考{@link com.everhomes.rest.techpark.punch.PunchExceptionRequestStatisticsItemDTO}</li>
 * </ul>
 */
public class PunchMonthlyStatisticsByDepartmentResponse {
    private String statisticsMonth;
    private Long departmentId;
    private Long lastUpdateTime;
    private List<PunchStatusStatisticsItemDTO> punchStatusStatisticsList;
    private List<PunchExceptionRequestStatisticsItemDTO> exceptionRequestStatisticsList;

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
