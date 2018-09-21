package com.everhomes.techpark.punch;

import com.everhomes.rest.techpark.punch.PunchType;
import com.everhomes.util.StringHelper;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Map;

public class PunchNotificationCommand {
    private Integer namespaceId;
    private Long organizationId;
    private Long punchRuleId;
    private java.sql.Date punchDate;
    private Timestamp punchRuleTime;
    private Timestamp flexOnDuty;
    private PunchType punchType;
    private Integer punchIntervalNo;
    private Timestamp remindTimeBetweenFrom;
    private Timestamp remindTimeBetweenTo;

    public PunchNotificationCommand() {

    }

    public PunchNotificationCommand(Map<String, Object> params) {
        if (params.containsKey("namespaceId")) {
            this.namespaceId = (Integer) params.get("namespaceId");
        }
        if (params.containsKey("organizationId")) {
            this.organizationId = (Long) params.get("organizationId");
        }
        if (params.containsKey("punchRuleId")) {
            this.punchRuleId = (Long) params.get("punchRuleId");
        }
        if (params.containsKey("punchDate")) {
            this.punchDate = (java.sql.Date) params.get("punchDate");
        }
        if (params.containsKey("punchRuleTime")) {
            this.punchRuleTime = (Timestamp) params.get("punchRuleTime");
        }
        if (params.containsKey("flexOnDuty")) {
            this.flexOnDuty = (Timestamp) params.get("flexOnDuty");
        }
        if (params.containsKey("punchType")) {
            this.punchType = PunchType.fromCode((byte) params.get("punchType"));
        }
        if (params.containsKey("punchIntervalNo")) {
            this.punchIntervalNo = (Integer) params.get("punchIntervalNo");
        }
    }

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

    public Timestamp getFlexOnDuty() {
        return flexOnDuty;
    }

    public void setFlexOnDuty(Timestamp flexOnDuty) {
        this.flexOnDuty = flexOnDuty;
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
