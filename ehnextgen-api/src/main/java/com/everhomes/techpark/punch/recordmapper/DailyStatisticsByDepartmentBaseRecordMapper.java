package com.everhomes.techpark.punch.recordmapper;

import com.everhomes.rest.techpark.punch.PunchExceptionRequestStatisticsItemType;
import com.everhomes.util.StringHelper;
import org.jooq.Record;
import org.jooq.RecordMapper;

public class DailyStatisticsByDepartmentBaseRecordMapper implements RecordMapper<Record, DailyStatisticsByDepartmentBaseRecordMapper>, PunchStatisticsParser {
    private Integer numOfRest;
    private Integer numOfShouldAttendance;
    private Integer numOfAttendanced;
    private Integer rateOfAttendance;

    public Integer getNumOfRest() {
        return numOfRest;
    }

    public void setNumOfRest(Integer numOfRest) {
        this.numOfRest = numOfRest;
    }

    public Integer getNumOfShouldAttendance() {
        return numOfShouldAttendance;
    }

    public void setNumOfShouldAttendance(Integer numOfShouldAttendance) {
        this.numOfShouldAttendance = numOfShouldAttendance;
    }

    public Integer getNumOfAttendanced() {
        return numOfAttendanced;
    }

    public void setNumOfAttendanced(Integer numOfAttendanced) {
        this.numOfAttendanced = numOfAttendanced;
    }

    public Integer getRateOfAttendance() {
        return rateOfAttendance;
    }

    public void setRateOfAttendance(Integer rateOfAttendance) {
        this.rateOfAttendance = rateOfAttendance;
    }

    @PunchExceptionRequestStatisticsItem(defaultOrder = 1, type = PunchExceptionRequestStatisticsItemType.ASK_FOR_LEAVE)
    private Integer askForLeaveRequestMemberCount;

    @PunchExceptionRequestStatisticsItem(defaultOrder = 2, type = PunchExceptionRequestStatisticsItemType.GO_OUT)
    private Integer goOutRequestMemberCount;

    @PunchExceptionRequestStatisticsItem(defaultOrder = 3, type = PunchExceptionRequestStatisticsItemType.BUSINESS_TRIP)
    private Integer businessTripRequestMemberCount;

    @PunchExceptionRequestStatisticsItem(defaultOrder = 4, type = PunchExceptionRequestStatisticsItemType.OVERTIME)
    private Integer overtimeRequestMemberCount;

    @PunchExceptionRequestStatisticsItem(defaultOrder = 5, type = PunchExceptionRequestStatisticsItemType.PUNCH_EXCEPTION)
    private Integer forgotPunchRequestMemberCount;

    public Integer getAskForLeaveRequestMemberCount() {
        return askForLeaveRequestMemberCount;
    }

    public void setAskForLeaveRequestMemberCount(Integer askForLeaveRequestMemberCount) {
        this.askForLeaveRequestMemberCount = askForLeaveRequestMemberCount;
    }

    public Integer getGoOutRequestMemberCount() {
        return goOutRequestMemberCount;
    }

    public void setGoOutRequestMemberCount(Integer goOutRequestMemberCount) {
        this.goOutRequestMemberCount = goOutRequestMemberCount;
    }

    public Integer getBusinessTripRequestMemberCount() {
        return businessTripRequestMemberCount;
    }

    public void setBusinessTripRequestMemberCount(Integer businessTripRequestMemberCount) {
        this.businessTripRequestMemberCount = businessTripRequestMemberCount;
    }

    public Integer getOvertimeRequestMemberCount() {
        return overtimeRequestMemberCount;
    }

    public void setOvertimeRequestMemberCount(Integer overtimeRequestMemberCount) {
        this.overtimeRequestMemberCount = overtimeRequestMemberCount;
    }

    public Integer getForgotPunchRequestMemberCount() {
        return forgotPunchRequestMemberCount;
    }

    public void setForgotPunchRequestMemberCount(Integer forgotPunchRequestMemberCount) {
        this.forgotPunchRequestMemberCount = forgotPunchRequestMemberCount;
    }

    @Override
    public DailyStatisticsByDepartmentBaseRecordMapper map(Record record) {
        return null;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
