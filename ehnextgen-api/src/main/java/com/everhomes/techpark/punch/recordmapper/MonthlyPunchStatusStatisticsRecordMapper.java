package com.everhomes.techpark.punch.recordmapper;

import com.everhomes.rest.techpark.punch.PunchStatusStatisticsItemType;
import com.everhomes.util.StringHelper;
import org.jooq.Record;
import org.jooq.RecordMapper;

public class MonthlyPunchStatusStatisticsRecordMapper implements RecordMapper<Record, MonthlyPunchStatusStatisticsRecordMapper>, PunchStatisticsParser {
    @PunchStatusStatisticsItem(defaultOrder = 1, type = PunchStatusStatisticsItemType.BELATE)
    private Integer belateMemberCount;

    @PunchStatusStatisticsItem(defaultOrder = 2, type = PunchStatusStatisticsItemType.LEAVE_EARLY)
    private Integer leaveEarlyMemberCount;

    @PunchStatusStatisticsItem(defaultOrder = 3, type = PunchStatusStatisticsItemType.ABSENT)
    private Integer absenceMemberCount;

    @PunchStatusStatisticsItem(defaultOrder = 4, type = PunchStatusStatisticsItemType.FORGOT_PUNCH)
    private Integer forgotPunchMemberCount;

    @PunchStatusStatisticsItem(defaultOrder = 5, type = PunchStatusStatisticsItemType.GO_OUT)
    private Integer goOutPunchDayCount;

    @PunchStatusStatisticsItem(defaultOrder = 6, type = PunchStatusStatisticsItemType.NORMAL)
    private Integer normalMemberCount;

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

    public Integer getForgotPunchMemberCount() {
        return forgotPunchMemberCount;
    }

    public void setForgotPunchMemberCount(Integer forgotPunchMemberCount) {
        this.forgotPunchMemberCount = forgotPunchMemberCount;
    }

    public Integer getNormalMemberCount() {
        return normalMemberCount;
    }

    public void setNormalMemberCount(Integer normalMemberCount) {
        this.normalMemberCount = normalMemberCount;
    }

    @Override
    public MonthlyPunchStatusStatisticsRecordMapper map(Record record) {
        MonthlyPunchStatusStatisticsRecordMapper data = new MonthlyPunchStatusStatisticsRecordMapper();
        data.setBelateMemberCount(record.getValue("belateMemberCount", Integer.class));
        data.setLeaveEarlyMemberCount(record.getValue("leaveEarlyMemberCount", Integer.class));
        data.setAbsenceMemberCount(record.getValue("absenceMemberCount", Integer.class));
        data.setForgotPunchMemberCount(record.getValue("forgotPunchMemberCount", Integer.class));
        data.setNormalMemberCount(record.getValue("normalMemberCount", Integer.class));
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
