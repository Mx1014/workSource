package com.everhomes.organization;

import com.everhomes.server.schema.tables.pojos.EhOrganizationMemberDetails;
import com.everhomes.util.StringHelper;

import java.sql.Date;

public class OrganizationMemberDetails extends EhOrganizationMemberDetails {

    private String targetType;
    private Long targetId;
    private String memberGroup;
    private Byte visibleFlag;
    private Date endTime;

    public OrganizationMemberDetails() {
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getMemberGroup() {
        return memberGroup;
    }

    public void setMemberGroup(String memberGroup) {
        this.memberGroup = memberGroup;
    }

    public Byte getVisibleFlag() {
        return visibleFlag;
    }

    public void setVisibleFlag(Byte visibleFlag) {
        this.visibleFlag = visibleFlag;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
