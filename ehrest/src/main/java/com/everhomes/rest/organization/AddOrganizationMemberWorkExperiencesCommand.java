package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>detailId: 员工标识号</li>
 * <li>enterpriseName: 公司名称</li>
 * <li>position: 职位</li>
 * <li>jobType: 工作类型</li>
 * <li>entryTime: 入职时间</li>
 * <li>departureTime: 离职时间</li>
 * </ul>
 */
public class AddOrganizationMemberWorkExperiencesCommand {

    @NotNull
    private Long detailId;

    @NotNull
    private String enterpriseName;

    @NotNull
    private String position;

    @NotNull
    private Byte jobType;

    @NotNull
    private String entryTime;

    @NotNull
    private String departureTime;

    public AddOrganizationMemberWorkExperiencesCommand() {
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Byte getJobType() {
        return jobType;
    }

    public void setJobType(Byte jobType) {
        this.jobType = jobType;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
