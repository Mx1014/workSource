package com.everhomes.techpark.punch.recordmapper;

import com.everhomes.util.StringHelper;
import org.jooq.Record;
import org.jooq.RecordMapper;

public class DailyStatisticsByDepartmentBaseRecordMapper implements RecordMapper<Record, DailyStatisticsByDepartmentBaseRecordMapper>, PunchStatisticsParser {
    private Integer restMemberCount;
    private Integer shouldArrivedMemberCount;
    private Integer arrivedMemberCount;
    private Integer rateOfAttendance;

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

    public Integer getArrivedMemberCount() {
        return arrivedMemberCount;
    }

    public void setArrivedMemberCount(Integer arrivedMemberCount) {
        this.arrivedMemberCount = arrivedMemberCount;
    }

    public Integer getRateOfAttendance() {
        return rateOfAttendance;
    }

    public void setRateOfAttendance(Integer rateOfAttendance) {
        this.rateOfAttendance = rateOfAttendance;
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
