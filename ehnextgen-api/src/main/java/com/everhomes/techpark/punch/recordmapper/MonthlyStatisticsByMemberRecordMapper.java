package com.everhomes.techpark.punch.recordmapper;

import com.everhomes.rest.techpark.punch.PunchExceptionRequestStatisticsItemType;
import com.everhomes.rest.techpark.punch.PunchStatusStatisticsItemType;
import com.everhomes.util.StringHelper;
import org.jooq.Record;
import org.jooq.RecordMapper;

public class MonthlyStatisticsByMemberRecordMapper implements RecordMapper<Record, MonthlyStatisticsByMemberRecordMapper>, PunchStatisticsParser {
    private Long id;
    private String punchMonth;
    private String ownerType;
    private Long ownerId;
    private Long userId;
    private Long detailId;
    private Integer workCount;
    private Integer restDayCount;

    @PunchStatusStatisticsItem(defaultOrder = 1, type = PunchStatusStatisticsItemType.BELATE)
    private Integer belateCount;

    @PunchStatusStatisticsItem(defaultOrder = 2, type = PunchStatusStatisticsItemType.LEAVE_EARLY)
    private Integer leaveEarlyCount;

    @PunchStatusStatisticsItem(defaultOrder = 3, type = PunchStatusStatisticsItemType.ABSENT)
    private Integer absenceCount;

    @PunchStatusStatisticsItem(defaultOrder = 4, type = PunchStatusStatisticsItemType.FORGOT_PUNCH)
    private Integer forgotCount;

    @PunchStatusStatisticsItem(defaultOrder = 5, type = PunchStatusStatisticsItemType.GO_OUT)
    private Integer goOutPunchDayCount;

    @PunchExceptionRequestStatisticsItem(defaultOrder = 1, type = PunchExceptionRequestStatisticsItemType.ASK_FOR_LEAVE)
    private Integer askForLeaveRequestCount;

    @PunchExceptionRequestStatisticsItem(defaultOrder = 2, type = PunchExceptionRequestStatisticsItemType.GO_OUT)
    private Integer goOutRequestCount;

    @PunchExceptionRequestStatisticsItem(defaultOrder = 3, type = PunchExceptionRequestStatisticsItemType.BUSINESS_TRIP)
    private Integer businessTripRequestCount;

    @PunchExceptionRequestStatisticsItem(defaultOrder = 4, type = PunchExceptionRequestStatisticsItemType.OVERTIME)
    private Integer overtimeRequestCount;

    @PunchExceptionRequestStatisticsItem(defaultOrder = 5, type = PunchExceptionRequestStatisticsItemType.PUNCH_EXCEPTION)
    private Integer punchExceptionRequestCount;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPunchMonth() {
        return punchMonth;
    }

    public void setPunchMonth(String punchMonth) {
        this.punchMonth = punchMonth;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
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

    public Integer getWorkCount() {
        return workCount;
    }

    public void setWorkCount(Integer workCount) {
        this.workCount = workCount;
    }

    public void setAbsenceCount(Integer absenceCount) {
        this.absenceCount = absenceCount;
    }

    public Integer getAbsenceCount() {
        return absenceCount;
    }

    public Integer getRestDayCount() {
        return restDayCount;
    }

    public void setRestDayCount(Integer restDayCount) {
        this.restDayCount = restDayCount;
    }

    public Integer getBelateCount() {
        return belateCount;
    }

    public void setBelateCount(Integer belateCount) {
        this.belateCount = belateCount;
    }

    public Integer getLeaveEarlyCount() {
        return leaveEarlyCount;
    }

    public void setLeaveEarlyCount(Integer leaveEarlyCount) {
        this.leaveEarlyCount = leaveEarlyCount;
    }

    public Integer getForgotCount() {
        return forgotCount;
    }

    public void setForgotCount(Integer forgotCount) {
        this.forgotCount = forgotCount;
    }

    public Integer getAskForLeaveRequestCount() {
        return askForLeaveRequestCount;
    }

    public void setAskForLeaveRequestCount(Integer askForLeaveRequestCount) {
        this.askForLeaveRequestCount = askForLeaveRequestCount;
    }

    public Integer getGoOutRequestCount() {
        return goOutRequestCount;
    }

    public void setGoOutRequestCount(Integer goOutRequestCount) {
        this.goOutRequestCount = goOutRequestCount;
    }

    public Integer getBusinessTripRequestCount() {
        return businessTripRequestCount;
    }

    public void setBusinessTripRequestCount(Integer businessTripRequestCount) {
        this.businessTripRequestCount = businessTripRequestCount;
    }

    public Integer getOvertimeRequestCount() {
        return overtimeRequestCount;
    }

    public void setOvertimeRequestCount(Integer overtimeRequestCount) {
        this.overtimeRequestCount = overtimeRequestCount;
    }

    public Integer getPunchExceptionRequestCount() {
        return punchExceptionRequestCount;
    }

    public void setPunchExceptionRequestCount(Integer punchExceptionRequestCount) {
        this.punchExceptionRequestCount = punchExceptionRequestCount;
    }

    @Override
    public MonthlyStatisticsByMemberRecordMapper map(Record record) {
        MonthlyStatisticsByMemberRecordMapper data = new MonthlyStatisticsByMemberRecordMapper();
        data.setId(record.getValue("id", Long.class));
        data.setPunchMonth(record.getValue("punchMonth", String.class));
        data.setOwnerType(record.getValue("ownerType", String.class));
        data.setOwnerId(record.getValue("ownerId", Long.class));
        data.setUserId(record.getValue("userId", Long.class));
        data.setDetailId(record.getValue("detailId", Long.class));
        data.setWorkCount(record.getValue("workCount", Integer.class));
        data.setRestDayCount(record.getValue("restDayCount", Integer.class));
        data.setBelateCount(record.getValue("belateCount", Integer.class));
        data.setLeaveEarlyCount(record.getValue("leaveEarlyCount", Integer.class));
        data.setAbsenceCount(record.getValue("absenceCount", Integer.class));
        data.setForgotCount(record.getValue("forgotCount", Integer.class));
        data.setAskForLeaveRequestCount(record.getValue("askForLeaveRequestCount", Integer.class));
        data.setGoOutRequestCount(record.getValue("goOutRequestCount", Integer.class));
        data.setBusinessTripRequestCount(record.getValue("businessTripRequestCount", Integer.class));
        data.setOvertimeRequestCount(record.getValue("overtimeRequestCount", Integer.class));
        data.setPunchExceptionRequestCount(record.getValue("punchExceptionRequestCount", Integer.class));
        data.setGoOutPunchDayCount(record.getValue("goOutPunchDayCount", Integer.class));
        return data;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Integer getGoOutPunchDayCount() {
        return goOutPunchDayCount;
    }

    public void setGoOutPunchDayCount(Integer goOutPunchDayCount) {
        this.goOutPunchDayCount = goOutPunchDayCount;
    }
}
