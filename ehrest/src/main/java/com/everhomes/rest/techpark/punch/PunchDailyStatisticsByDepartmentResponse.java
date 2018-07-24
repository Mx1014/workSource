package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * <ul>
 * <li>statisticsDate: 统计日期，时间戳</li>
 * <li>departmentId: 部门id</li>
 * <li>restMemberCount: 当日休息的人数，参考{@link com.everhomes.rest.techpark.punch.PunchStatusStatisticsItemDTO}</li>
 * <li>shouldArrivedMemberCount: 当日应到（应出勤）的人数，参考{@link com.everhomes.rest.techpark.punch.PunchStatusStatisticsItemDTO}</li>
 * <li>arrivedMemberCount: 当日已到（已出勤）的人数，参考{@link com.everhomes.rest.techpark.punch.PunchStatusStatisticsItemDTO}</li>
 * <li>rateOfAttendance: 出勤率</li>
 * <li>punchStatusStatisticsList: 未到、迟到、早退等其它出勤统计项，参考{@link com.everhomes.rest.techpark.punch.PunchStatusStatisticsItemDTO}</li>
 * <li>exceptionRequestStatisticsList: 请假、出差等申请统计项，参考{@link com.everhomes.rest.techpark.punch.PunchExceptionRequestStatisticsItemDTO}</li>
 * </ul>
 */
public class PunchDailyStatisticsByDepartmentResponse {
    private Long statisticsDate;
    private Long departmentId;
    private PunchStatusStatisticsItemDTO restMemberCount;
    private PunchStatusStatisticsItemDTO shouldArrivedMemberCount;
    private PunchStatusStatisticsItemDTO arrivedMemberCount;
    private Integer rateOfAttendance;

    private List<PunchStatusStatisticsItemDTO> punchStatusStatisticsList;
    private List<PunchExceptionRequestStatisticsItemDTO> exceptionRequestStatisticsList;

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

    public PunchStatusStatisticsItemDTO getRestMemberCount() {
        return restMemberCount;
    }

    public void setRestMemberCount(PunchStatusStatisticsItemDTO restMemberCount) {
        this.restMemberCount = restMemberCount;
    }

    public PunchStatusStatisticsItemDTO getShouldArrivedMemberCount() {
        return shouldArrivedMemberCount;
    }

    public void setShouldArrivedMemberCount(PunchStatusStatisticsItemDTO shouldArrivedMemberCount) {
        this.shouldArrivedMemberCount = shouldArrivedMemberCount;
    }

    public PunchStatusStatisticsItemDTO getArrivedMemberCount() {
        return arrivedMemberCount;
    }

    public void setArrivedMemberCount(PunchStatusStatisticsItemDTO arrivedMemberCount) {
        this.arrivedMemberCount = arrivedMemberCount;
    }

    public Integer getRateOfAttendance() {
        return rateOfAttendance;
    }

    public void setRateOfAttendance(Integer rateOfAttendance) {
        this.rateOfAttendance = rateOfAttendance;
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
