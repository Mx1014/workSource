package com.everhomes.organization;

import com.everhomes.server.schema.tables.pojos.EhOrganizationMemberDetails;
import com.everhomes.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;

public class OrganizationMemberDetails extends EhOrganizationMemberDetails {

    @Autowired
    OrganizationProvider organizationProvider;

    private String targetType;
    private Long targetId;
    private String memberGroup;
    private Byte visibleFlag;
    private Date endTime;
    private String nickName;

    public OrganizationMemberDetails() {
    }


    public OrganizationMemberDetails(OrganizationMember member) {
        //需要判断organizationMember在detail表中organization_id的取值。应该取公司或者子公司
        Long directOrgId = 0L;
        if(member.getGroupType().equals("ENTERPRISE")){
            directOrgId = member.getOrganizationId();
        }else {
            Organization org = organizationProvider.findOrganizationById(member.getOrganizationId());
            directOrgId = org.getDirectlyEnterpriseId();
        }

        this.setId(member.getDetailId());
        this.setNamespaceId(member.getNamespaceId());
        this.setOrganizationId(directOrgId);
        this.setContactName(member.getContactName());
        this.setContactToken(member.getContactToken());
        this.setContactDescription(member.getContactDescription());
        this.setEmployeeNo(member.getEmployeeNo());
        this.setAvatar(member.getAvatar());
        this.setGender(member.getGender());
        this.setEmployeeStatus(member.getEmployeeStatus());
        this.setEmploymentTime(member.getEmploymentTime());
        this.setProfileIntegrity(member.getProfileIntegrity());
        this.setCheckInTime(member.getCheckInTime());
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
