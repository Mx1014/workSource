package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>detailId: 员工标识号</li>
 * <li>namespaceId: 命名空间，参考员工members表的命名空间</li>
 * <li>enterpriseName: 公司名称</li>
 * <li>position: 职位</li>
 * <li>jobType: 工作类型</li>
 * <li>entryTime: 入职时间</li>
 * <li>departureTime: 离职时间</li>
 * <li>creatorUid: 创建时间</li>
 * </ul>
 */
public class AddOrganizationMemberWorkExperiencesCommand {

    private Long detailId;

    private Integer namespaceId;

    private String enterpriseName;

    private String position;

    private Byte jobType;

    private String entryTime;

    private String departureTime;

    private Long creatorUid;

    public AddOrganizationMemberWorkExperiencesCommand() {
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
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

    public Long getCreatorUid() {
        return creatorUid;
    }

    public void setCreatorUid(Long creatorUid) {
        this.creatorUid = creatorUid;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
