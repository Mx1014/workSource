package com.everhomes.techpark.punch.recordmapper;

import com.everhomes.rest.techpark.punch.PunchExceptionRequestStatisticsItemType;
import com.everhomes.util.StringHelper;
import org.jooq.Record;
import org.jooq.RecordMapper;

public class PunchExceptionRequestStatisticsRecordMapper implements RecordMapper<Record, PunchExceptionRequestStatisticsRecordMapper>, PunchStatisticsParser {
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
    public PunchExceptionRequestStatisticsRecordMapper map(Record record) {
        PunchExceptionRequestStatisticsRecordMapper data = new PunchExceptionRequestStatisticsRecordMapper();
        data.setAskForLeaveRequestMemberCount(record.getValue("askForLeaveRequestMemberCount", Integer.class));
        data.setGoOutRequestMemberCount(record.getValue("goOutRequestMemberCount", Integer.class));
        data.setBusinessTripRequestMemberCount(record.getValue("businessTripRequestMemberCount", Integer.class));
        data.setOvertimeRequestMemberCount(record.getValue("overtimeRequestMemberCount", Integer.class));
        data.setPunchExceptionRequestCount(record.getValue("punchExceptionRequestCount", Integer.class));
        return data;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
