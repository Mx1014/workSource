package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *<li>id: 工作经验信息编号</li>
 * <li>enterpriseName: 公司名称</li>
 * <li>position: 职位</li>
 * <li>jobType: 工作类型</li>
 * <li>entryTime: 入职时间</li>
 * <li>departureTime: 离职时间</li>
 * </ul>
 */
public class UpdateOrganizationMemberWorkExperiencesCommand {

    private Long id;

    private String enterpriseName;

    private String position;

    private String jobType;

    private String entryTime;

    private String departureTime;

    public UpdateOrganizationMemberWorkExperiencesCommand() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
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
