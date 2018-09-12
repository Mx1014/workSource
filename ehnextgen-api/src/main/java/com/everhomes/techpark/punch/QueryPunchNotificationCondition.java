package com.everhomes.techpark.punch;

import com.everhomes.rest.techpark.punch.PunchType;
import com.everhomes.util.StringHelper;

import java.sql.Date;
import java.sql.Timestamp;

public class QueryPunchNotificationCondition {
    private Integer namespaceId;
    private Long organizationId;
    private Long punchRuleId;
    private java.sql.Date punchDate;
    private Timestamp punchRuleTime;
    private PunchType punchType;
    private Integer punchIntervalNo;
    private Timestamp remindTimeBetweenFrom;
    private Timestamp remindTimeBetweenTo;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getPunchRuleId() {
        return punchRuleId;
    }

    public void setPunchRuleId(Long punchRuleId) {
        this.punchRuleId = punchRuleId;
    }

    public Date getPunchDate() {
        return punchDate;
    }

    public void setPunchDate(Date punchDate) {
        this.punchDate = punchDate;
    }

    public Timestamp getPunchRuleTime() {
        return punchRuleTime;
    }

    public void setPunchRuleTime(Timestamp punchRuleTime) {
        this.punchRuleTime = punchRuleTime;
    }

    public PunchType getPunchType() {
        return punchType;
    }

    public void setPunchType(PunchType punchType) {
        this.punchType = punchType;
    }

    public Integer getPunchIntervalNo() {
        return punchIntervalNo;
    }

    public void setPunchIntervalNo(Integer punchIntervalNo) {
        this.punchIntervalNo = punchIntervalNo;
    }

    public Timestamp getRemindTimeBetweenFrom() {
        return remindTimeBetweenFrom;
    }

    public void setRemindTimeBetweenFrom(Timestamp remindTimeBetweenFrom) {
        this.remindTimeBetweenFrom = remindTimeBetweenFrom;
    }

    public Timestamp getRemindTimeBetweenTo() {
        return remindTimeBetweenTo;
    }

    public void setRemindTimeBetweenTo(Timestamp remindTimeBetweenTo) {
        this.remindTimeBetweenTo = remindTimeBetweenTo;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
