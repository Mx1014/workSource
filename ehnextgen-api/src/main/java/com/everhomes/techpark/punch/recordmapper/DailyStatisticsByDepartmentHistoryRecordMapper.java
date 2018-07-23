package com.everhomes.techpark.punch.recordmapper;

import com.everhomes.rest.techpark.punch.PunchStatusStatisticsItemType;
import com.everhomes.util.StringHelper;
import org.jooq.Record;

public class DailyStatisticsByDepartmentHistoryRecordMapper extends DailyStatisticsByDepartmentBaseRecordMapper {
    private Integer restMemberCount;
    private Integer shouldArrivedMemberCount;
    private Integer actArrivedMemberCount;
    private Integer unArrivedMemberCount;

    @PunchStatusStatisticsItem(defaultOrder = 1, type = PunchStatusStatisticsItemType.ABSENT)
    private Integer absenceMemberCount;

    @PunchStatusStatisticsItem(defaultOrder = 2, type = PunchStatusStatisticsItemType.FORGOT_PUNCH)
    private Integer forgotPunchMemberCount;

    @PunchStatusStatisticsItem(defaultOrder = 3, type = PunchStatusStatisticsItemType.BELATE)
    private Integer belateMemberCount;

    @PunchStatusStatisticsItem(defaultOrder = 4, type = PunchStatusStatisticsItemType.LEAVE_EARLY)
    private Integer leaveEarlyMemberCount;

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

    public Integer getAbsenceMemberCount() {
        return absenceMemberCount;
    }

    public void setAbsenceMemberCount(Integer absenceMemberCount) {
        this.absenceMemberCount = absenceMemberCount;
    }

    public Integer getForgotPunchMemberCount() {
        return forgotPunchMemberCount;
    }

    public void setForgotPunchMemberCount(Integer forgotPunchMemberCount) {
        this.forgotPunchMemberCount = forgotPunchMemberCount;
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


    @Override
    public DailyStatisticsByDepartmentHistoryRecordMapper map(Record record) {
        DailyStatisticsByDepartmentHistoryRecordMapper data = new DailyStatisticsByDepartmentHistoryRecordMapper();
        data.setRestMemberCount(record.getValue("restMemberCount", Integer.class));
        data.setShouldArrivedMemberCount(record.getValue("shouldArrivedMemberCount", Integer.class));
        data.setActArrivedMemberCount(record.getValue("actArrivedMemberCount", Integer.class));
        data.setUnArrivedMemberCount(record.getValue("unArrivedMemberCount", Integer.class));
        data.setAbsenceMemberCount(record.getValue("absenceMemberCount", Integer.class));
        data.setForgotPunchMemberCount(record.getValue("forgotPunchMemberCount", Integer.class));
        data.setBelateMemberCount(record.getValue("belateMemberCount", Integer.class));
        data.setLeaveEarlyMemberCount(record.getValue("leaveEarlyMemberCount", Integer.class));
        data.setAskForLeaveRequestMemberCount(record.getValue("askForLeaveRequestMemberCount", Integer.class));
        data.setGoOutRequestMemberCount(record.getValue("goOutRequestMemberCount", Integer.class));
        data.setBusinessTripRequestMemberCount(record.getValue("businessTripRequestMemberCount", Integer.class));
        data.setOvertimeRequestMemberCount(record.getValue("overtimeRequestMemberCount", Integer.class));
        data.setForgotPunchRequestMemberCount(record.getValue("forgotPunchRequestMemberCount", Integer.class));
        data.setNumOfRest(data.getRestMemberCount() == null ? 0 : data.getRestMemberCount());
        data.setNumOfShouldAttendance(data.getShouldArrivedMemberCount() == null ? 0 : data.getShouldArrivedMemberCount());
        data.setNumOfAttendanced(data.getActArrivedMemberCount() == null ? 0 : data.getActArrivedMemberCount());
        data.setRateOfAttendance(data.getShouldArrivedMemberCount() == 0 ? 0 : (int) Math.ceil((double) data.getNumOfAttendanced() * 100 / data.getNumOfShouldAttendance()));
        return data;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
