package com.everhomes.techpark.punch.recordmapper;

import com.everhomes.rest.techpark.punch.PunchExceptionRequestStatisticsItemType;
import com.everhomes.rest.techpark.punch.PunchStatusStatisticsItemType;
import com.everhomes.util.StringHelper;
import org.jooq.Record;

public class DailyStatisticsByDepartmentTodayRecordMapper extends DailyStatisticsByDepartmentBaseRecordMapper {

    @PunchStatusStatisticsItem(defaultOrder = 1, type = PunchStatusStatisticsItemType.UN_ARRIVED)
    private Integer unArrivedMemberCount;

    @PunchStatusStatisticsItem(defaultOrder = 2, type = PunchStatusStatisticsItemType.BELATE)
    private Integer belateMemberCount;

    @PunchStatusStatisticsItem(defaultOrder = 3, type = PunchStatusStatisticsItemType.LEAVE_EARLY)
    private Integer leaveEarlyMemberCount;

    @PunchStatusStatisticsItem(defaultOrder = 4, type = PunchStatusStatisticsItemType.GO_OUT)
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

    public Integer getUnArrivedMemberCount() {
        return unArrivedMemberCount;
    }

    public void setUnArrivedMemberCount(Integer unArrivedMemberCount) {
        this.unArrivedMemberCount = unArrivedMemberCount;
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
    public DailyStatisticsByDepartmentTodayRecordMapper map(Record record) {
        DailyStatisticsByDepartmentTodayRecordMapper data = new DailyStatisticsByDepartmentTodayRecordMapper();
        data.setRestMemberCount(record.getValue("restMemberCount", Integer.class));
        data.setShouldArrivedMemberCount(record.getValue("shouldArrivedMemberCount", Integer.class));
        data.setArrivedMemberCount(record.getValue("actArrivedMemberCount", Integer.class));
        data.setUnArrivedMemberCount(record.getValue("unArrivedMemberCount", Integer.class));
        data.setBelateMemberCount(record.getValue("belateMemberCount", Integer.class));
        data.setLeaveEarlyMemberCount(record.getValue("leaveEarlyMemberCount", Integer.class));
        data.setAskForLeaveRequestMemberCount(record.getValue("askForLeaveRequestMemberCount", Integer.class));
        data.setGoOutRequestMemberCount(record.getValue("goOutRequestMemberCount", Integer.class));
        data.setBusinessTripRequestMemberCount(record.getValue("businessTripRequestMemberCount", Integer.class));
        data.setOvertimeRequestMemberCount(record.getValue("overtimeRequestMemberCount", Integer.class));
        data.setPunchExceptionRequestCount(record.getValue("punchExceptionRequestCount", Integer.class));
        data.setGoOutPunchDayCount(record.getValue("goOutPunchDayCount", Integer.class));
        if (data.getShouldArrivedMemberCount() == null || data.getShouldArrivedMemberCount() <= 0
                || data.getArrivedMemberCount() == null || data.getArrivedMemberCount() <= 0) {
            data.setRateOfAttendance(0);
        } else {
            data.setRateOfAttendance((int) Math.ceil((double) data.getArrivedMemberCount() * 100 / data.getShouldArrivedMemberCount()));
        }
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
