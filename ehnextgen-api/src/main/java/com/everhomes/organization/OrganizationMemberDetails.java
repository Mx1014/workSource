package com.everhomes.organization;

import com.everhomes.server.schema.tables.pojos.EhOrganizationMemberDetails;
import com.everhomes.util.DateHelper;
import com.everhomes.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class  OrganizationMemberDetails extends EhOrganizationMemberDetails {

    private String targetType;
    private Long targetId;
    private String memberGroup;
    private Byte visibleFlag;
    private Date endTime;
    private String nickName;

    //  add by ryan for socialSecurity
    private String departmentName;

    private String jobPositionName;

    private String jobLevelName;

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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getJobPositionName() {
        return jobPositionName;
    }

    public void setJobPositionName(String jobPositionName) {
        this.jobPositionName = jobPositionName;
    }

    public String getJobLevelName() {
        return jobLevelName;
    }

    public void setJobLevelName(String jobLevelName) {
        this.jobLevelName = jobLevelName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
