package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * Created by Ryan on 2017/5/18.
 * <ul>
 * <li>memberId: 员工编号</li>
 * <li>enterpriseName: 公司名称</li>
 * <li>position: 职位</li>
 * <li>jobType: 工作类型</li>
 * <li>entryTime: 入职时间</li>
 * <li>departureTime: 离职时间</li>
 * </ul>
 */
public class AddOrganizationMemberWorkExpsCommand {

    private Long memberId;

    private String enterpriseName;

    private String position;

    private String jobType;

    private String entryTime;

    private String departureTime;

    public AddOrganizationMemberWorkExpsCommand() {
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
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
