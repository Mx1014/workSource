package com.everhomes.techpark.punch.recordmapper;

import com.everhomes.rest.techpark.punch.PunchExceptionRequestStatisticsItemType;
import com.everhomes.rest.techpark.punch.PunchStatusStatisticsItemType;
import com.everhomes.util.StringHelper;
import org.jooq.Record;
import org.jooq.RecordMapper;

public class DailyStatisticsByDepartmentRecordMapper implements RecordMapper<Record, DailyStatisticsByDepartmentRecordMapper>, PunchStatisticsParser {
    @PunchStatusStatisticsItem(defaultOrder = 1, type = PunchStatusStatisticsItemType.REST)
    private Integer restMemberCount;

    private Integer shouldArrivedMemberCount;
    private Integer actArrivedMemberCount;

    @PunchStatusStatisticsItem(defaultOrder = 2, type = PunchStatusStatisticsItemType.UN_ATTENDANCE)
    private Integer unArrivedMemberCount;

    @PunchStatusStatisticsItem(defaultOrder = 3, type = PunchStatusStatisticsItemType.ABSENT)
    private Integer absentMemberCount;

    @PunchStatusStatisticsItem(defaultOrder = 4, type = PunchStatusStatisticsItemType.FORGOT_PUNCH)
    private Integer forgotPunchMemberCount;

    @PunchStatusStatisticsItem(defaultOrder = 5, type = PunchStatusStatisticsItemType.BELATE)
    private Integer belateMemberCount;

    @PunchStatusStatisticsItem(defaultOrder = 6, type = PunchStatusStatisticsItemType.LEAVE_EARLY)
    private Integer leaveEarlyMemberCount;

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

    public Integer getRestMemberCount() {
        return restMemberCount;
    }

    public void setRestMemberCount(Integer restMemberCount) {
        this.restMemberCount = restMemberCount;
    }

    public Integer getShouldArrivedMemberCount() {
        return shouldArrivedMemberCount;
    }

    public void setShouldArrivedMemberCount(Integer shouldArrivedMemberCount) {
        this.shouldArrivedMemberCount = shouldArrivedMemberCount;
    }

    public Integer getActArrivedMemberCount() {
        return actArrivedMemberCount;
    }

    public void setActArrivedMemberCount(Integer actArrivedMemberCount) {
        this.actArrivedMemberCount = actArrivedMemberCount;
    }

    public Integer getUnArrivedMemberCount() {
        return unArrivedMemberCount;
    }

    public void setUnArrivedMemberCount(Integer unArrivedMemberCount) {
        this.unArrivedMemberCount = unArrivedMemberCount;
    }

    public Integer getAbsentMemberCount() {
        return absentMemberCount;
    }

    public void setAbsentMemberCount(Integer absentMemberCount) {
        this.absentMemberCount = absentMemberCount;
    }

    public Integer getBelateMemberCount() {
        return belateMemberCount;
    }

    public void setBelateMemberCount(Integer belateMemberCount) {
        this.belateMemberCount = belateMemberCount;
    }

    public Integer getLeaveEarlyMemberCount() {
        return leaveEarlyMemberCount;
    }

    public void setLeaveEarlyMemberCount(Integer leaveEarlyMemberCount) {
        this.leaveEarlyMemberCount = leaveEarlyMemberCount;
    }

    public Integer getForgotPunchMemberCount() {
        return forgotPunchMemberCount;
    }

    public void setForgotPunchMemberCount(Integer forgotPunchMemberCount) {
        this.forgotPunchMemberCount = forgotPunchMemberCount;
    }

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
    public DailyStatisticsByDepartmentRecordMapper map(Record record) {
        DailyStatisticsByDepartmentRecordMapper data = new DailyStatisticsByDepartmentRecordMapper();
        data.setRestMemberCount(record.getValue("restMemberCount", Integer.class));
        data.setShouldArrivedMemberCount(record.getValue("shouldArrivedMemberCount", Integer.class));
        data.setActArrivedMemberCount(record.getValue("actArrivedMemberCount", Integer.class));
        data.setUnArrivedMemberCount(record.getValue("unArrivedMemberCount", Integer.class));
        data.setAbsentMemberCount(record.getValue("absentMemberCount", Integer.class));
        data.setForgotPunchMemberCount(record.getValue("forgotPunchMemberCount", Integer.class));
        data.setBelateMemberCount(record.getValue("belateMemberCount", Integer.class));
        data.setLeaveEarlyMemberCount(record.getValue("leaveEarlyMemberCount", Integer.class));
        data.setAskForLeaveRequestMemberCount(record.getValue("askForLeaveRequestMemberCount", Integer.class));
        data.setGoOutRequestMemberCount(record.getValue("goOutRequestMemberCount", Integer.class));
        data.setBusinessTripRequestMemberCount(record.getValue("businessTripRequestMemberCount", Integer.class));
        data.setOvertimeRequestMemberCount(record.getValue("overtimeRequestMemberCount", Integer.class));
        data.setForgotPunchRequestMemberCount(record.getValue("forgotPunchRequestMemberCount", Integer.class));
        return data;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
