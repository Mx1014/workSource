package com.everhomes.techpark.punch.recordmapper;

import com.everhomes.rest.techpark.punch.PunchExceptionRequestStatisticsItemType;
import com.everhomes.rest.techpark.punch.PunchStatusStatisticsItemType;
import com.everhomes.util.StringHelper;
import org.jooq.Record;
import org.jooq.RecordMapper;

public class MonthlyStatisticsByDepartmentRecordMapper implements RecordMapper<Record, MonthlyStatisticsByDepartmentRecordMapper>, PunchStatisticsParser {
    @PunchStatusStatisticsItem(defaultOrder = 1, type = PunchStatusStatisticsItemType.BELATE)
    private Integer belateMemberCount;

    @PunchStatusStatisticsItem(defaultOrder = 2, type = PunchStatusStatisticsItemType.LEAVE_EARLY)
    private Integer leaveEarlyMemberCount;

    @PunchStatusStatisticsItem(defaultOrder = 3, type = PunchStatusStatisticsItemType.ABSENT)
    private Integer absenceMemberCount;

    @PunchStatusStatisticsItem(defaultOrder = 4, type = PunchStatusStatisticsItemType.FORGOT_PUNCH)
    private Integer forgotMemberCount;

    @PunchStatusStatisticsItem(defaultOrder = 5, type = PunchStatusStatisticsItemType.NORMAL)
    private Integer normalMemberCount;

    @PunchStatusStatisticsItem(defaultOrder = 6, type = PunchStatusStatisticsItemType.GO_OUT)
    private Integer goOutPunchDayCount;

    @PunchExceptionRequestStatisticsItem(defaultOrder = 1, type = PunchExceptionRequestStatisticsItemType.ASK_FOR_LEAVE)
    private Integer askForLeaveRequestMemberCount;

    @PunchExceptionRequestStatisticsItem(defaultOrder = 2, type = PunchExceptionRequestStatisticsItemType.GO_OUT)
    private Integer goOutRequestMemberCount;

    @PunchExceptionRequestStatisticsItem(defaultOrder = 3, type = PunchExceptionRequestStatisticsItemType.BUSINESS_TRIP)
    private Integer businessTripRequestMemberCount;

    @PunchExceptionRequestStatisticsItem(defaultOrder = 4, type = PunchExceptionRequestStatisticsItemType.OVERTIME)
    private Integer overtimeRequestMemberCount;

    @PunchExceptionRequestStatisticsItem(defaultOrder = 5, type = PunchExceptionRequestStatisticsItemType.PUNCH_EXCEPTION)
    private Integer punchExceptionRequestCount;

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

    public Integer getAbsenceMemberCount() {
        return absenceMemberCount;
    }

    public void setAbsenceMemberCount(Integer absenceMemberCount) {
        this.absenceMemberCount = absenceMemberCount;
    }

    public Integer getForgotMemberCount() {
        return forgotMemberCount;
    }

    public void setForgotMemberCount(Integer forgotMemberCount) {
        this.forgotMemberCount = forgotMemberCount;
    }

    public Integer getNormalMemberCount() {
        return normalMemberCount;
    }

    public void setNormalMemberCount(Integer normalMemberCount) {
        this.normalMemberCount = normalMemberCount;
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

    public Integer getPunchExceptionRequestCount() {
        return punchExceptionRequestCount;
    }

    public void setPunchExceptionRequestCount(Integer punchExceptionRequestCount) {
        this.punchExceptionRequestCount = punchExceptionRequestCount;
    }

    @Override
    public MonthlyStatisticsByDepartmentRecordMapper map(Record record) {
        MonthlyStatisticsByDepartmentRecordMapper data = new MonthlyStatisticsByDepartmentRecordMapper();
        data.setBelateMemberCount(record.getValue("belateMemberCount", Integer.class));
        data.setLeaveEarlyMemberCount(record.getValue("leaveEarlyMemberCount", Integer.class));
        data.setAbsenceMemberCount(record.getValue("absenceMemberCount", Integer.class));
        data.setForgotMemberCount(record.getValue("forgotMemberCount", Integer.class));
        data.setNormalMemberCount(record.getValue("normalMemberCount", Integer.class));
        data.setAskForLeaveRequestMemberCount(record.getValue("askForLeaveRequestMemberCount", Integer.class));
        data.setGoOutRequestMemberCount(record.getValue("goOutRequestMemberCount", Integer.class));
        data.setBusinessTripRequestMemberCount(record.getValue("businessTripRequestMemberCount", Integer.class));
        data.setOvertimeRequestMemberCount(record.getValue("overtimeRequestMemberCount", Integer.class));
        data.setGoOutPunchDayCount(record.getValue("goOutPunchDayCount", Integer.class));
        data.setPunchExceptionRequestCount(record.getValue("punchExceptionRequestCount", Integer.class));
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
