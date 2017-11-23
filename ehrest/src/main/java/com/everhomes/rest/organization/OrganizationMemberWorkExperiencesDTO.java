package com.everhomes.rest.organization;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * <ul>
 * <li>id: 工作经历标识号</li>
 * <li>detailId: 员工标识号</li>
 * <li>enterpriseName：企业名称</li>
 * <li>position: 职位</li>
 * <li>jobType: 工作类型，参考{@link EmployeeType}</li>
 * <li>entryTime: 入职日期</li>
 * <li>departureTime: 离职日期</li>
 * </ul>
 */
public class OrganizationMemberWorkExperiencesDTO {

    private Long id;

    private Long detailId;

    private String enterpriseName;

    private String position;

    private Byte jobType;

    private Date entryTime;

    private Date departureTime;


    public OrganizationMemberWorkExperiencesDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Date entryTime) {
        this.entryTime = entryTime;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }
}
