package com.everhomes.techpark.punch.recordmapper;

import com.everhomes.rest.techpark.punch.PunchStatusStatisticsItemType;
import com.everhomes.util.StringHelper;
import org.jooq.Record;
import org.jooq.RecordMapper;

public class PunchStatusStatisticsRecordMapper implements RecordMapper<Record, PunchStatusStatisticsRecordMapper>, PunchStatisticsParser {
    @PunchStatusStatisticsItem(defaultOrder = 1, type = PunchStatusStatisticsItemType.UN_ATTENDANCE)
    private Integer unArrivedMemberCount;

    @PunchStatusStatisticsItem(defaultOrder = 2, type = PunchStatusStatisticsItemType.BELATE)
    private Integer belateMemberCount;

    @PunchStatusStatisticsItem(defaultOrder = 3, type = PunchStatusStatisticsItemType.LEAVE_EARLY)
    private Integer leaveEarlyMemberCount;

    @PunchStatusStatisticsItem(defaultOrder = 4, type = PunchStatusStatisticsItemType.ABSENT)
    private Integer absentMemberCount;

    @PunchStatusStatisticsItem(defaultOrder = 5, type = PunchStatusStatisticsItemType.FORGOT_PUNCH)
    private Integer forgotPunchMemberCount;

    @PunchStatusStatisticsItem(defaultOrder = 6, type = PunchStatusStatisticsItemType.NORMAL)
    private Integer normalMemberCount;

    @PunchStatusStatisticsItem(defaultOrder = 7, type = PunchStatusStatisticsItemType.REST)
    private Integer restMemberCount;

    private Integer shouldArrivedMemberCount;
    private Integer actArrivedMemberCount;


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

    public Integer getNormalMemberCount() {
        return normalMemberCount;
    }

    public void setNormalMemberCount(Integer normalMemberCount) {
        this.normalMemberCount = normalMemberCount;
    }

    @Override
    public PunchStatusStatisticsRecordMapper map(Record record) {
        PunchStatusStatisticsRecordMapper data = new PunchStatusStatisticsRecordMapper();
        data.setRestMemberCount(record.getValue("restMemberCount", Integer.class));
        data.setShouldArrivedMemberCount(record.getValue("shouldArrivedMemberCount", Integer.class));
        data.setActArrivedMemberCount(record.getValue("actArrivedMemberCount", Integer.class));
        data.setUnArrivedMemberCount(record.getValue("unArrivedMemberCount", Integer.class));
        data.setAbsentMemberCount(record.getValue("absentMemberCount", Integer.class));
        data.setForgotPunchMemberCount(record.getValue("forgotPunchMemberCount", Integer.class));
        data.setBelateMemberCount(record.getValue("belateMemberCount", Integer.class));
        data.setLeaveEarlyMemberCount(record.getValue("leaveEarlyMemberCount", Integer.class));
        return data;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
