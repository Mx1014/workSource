package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>detailId: 员工标识号</li>
 * <li>namespaceId: 域名空间</li>
 * <li>name: 保险名称</li>
 * <li>enterprise: 保险公司名称</li>
 * <li>number: 保险编号</li>
 * <li>startTime: 生效时间</li>
 * <li>endTime: 到期时间</li>
 * <li>creatorUid: 创建人</li>
 * </ul>
 */
public class AddOrganizationMemberInsurancesCommand {

    private Long detailId;

    private Integer namespaceId;

    private String name;

    private String enterprise;

    private String number;

    private String startTime;

    private String endTime;

    private Long creatorUid;

    public AddOrganizationMemberInsurancesCommand() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(String enterprise) {
        this.enterprise = enterprise;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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
