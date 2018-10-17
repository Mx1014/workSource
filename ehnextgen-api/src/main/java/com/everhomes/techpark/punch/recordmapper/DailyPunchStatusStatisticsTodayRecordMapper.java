package com.everhomes.techpark.punch.recordmapper;

import com.everhomes.rest.techpark.punch.PunchStatusStatisticsItemType;
import com.everhomes.util.StringHelper;

import org.jooq.Record;
import org.jooq.RecordMapper;

public class DailyPunchStatusStatisticsTodayRecordMapper implements RecordMapper<Record, DailyPunchStatusStatisticsTodayRecordMapper>, PunchStatisticsParser {
    @PunchStatusStatisticsItem(defaultOrder = 1, type = PunchStatusStatisticsItemType.UN_ARRIVED)
    private Integer unArrivedMemberCount;

    @PunchStatusStatisticsItem(defaultOrder = 2, type = PunchStatusStatisticsItemType.BELATE)
    private Integer belateMemberCount;

    @PunchStatusStatisticsItem(defaultOrder = 3, type = PunchStatusStatisticsItemType.LEAVE_EARLY)
    private Integer leaveEarlyMemberCount;

    @PunchStatusStatisticsItem(defaultOrder = 4, type = PunchStatusStatisticsItemType.NORMAL)
    private Integer normalMemberCount;

    @PunchStatusStatisticsItem(defaultOrder = 5, type = PunchStatusStatisticsItemType.REST)
    private Integer restMemberCount;

    @PunchStatusStatisticsItem(defaultOrder = 6, type = PunchStatusStatisticsItemType.GO_OUT)
    private Integer goOutPunchDayCount;

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

    public Integer getNormalMemberCount() {
        return normalMemberCount;
    }

    public void setNormalMemberCount(Integer normalMemberCount) {
        this.normalMemberCount = normalMemberCount;
    }

    public Integer getRestMemberCount() {
        return restMemberCount;
    }

    public void setRestMemberCount(Integer restMemberCount) {
        this.restMemberCount = restMemberCount;
    }

    @Override
    public DailyPunchStatusStatisticsTodayRecordMapper map(Record record) {
        DailyPunchStatusStatisticsTodayRecordMapper data = new DailyPunchStatusStatisticsTodayRecordMapper();
        data.setUnArrivedMemberCount(record.getValue("unArrivedMemberCount", Integer.class));
        data.setBelateMemberCount(record.getValue("belateMemberCount", Integer.class));
        data.setLeaveEarlyMemberCount(record.getValue("leaveEarlyMemberCount", Integer.class));
        data.setNormalMemberCount(record.getValue("normalMemberCount", Integer.class));
        data.setRestMemberCount(record.getValue("restMemberCount", Integer.class));
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
