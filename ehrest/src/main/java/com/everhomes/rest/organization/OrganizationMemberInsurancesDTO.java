package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

import java.sql.Date;

/**
 * <ul>
 * <li>id: 保险标识号</li>
 * <li>detailId: 员工标识号</li>
 * <li>name: 保险名称</li>
 * <li>enterprise: 保险公司名称</li>
 * <li>number: 保险编号</li>
 * <li>startTime: 生效时间</li>
 * <li>endTime: 到期时间</li>
 * </ul>
 */
public class OrganizationMemberInsurancesDTO {

    private Long id;

    private Long detailId;

    private String name;

    private String enterprise;

    private String number;

    private Date startTime;

    private Date endTime;

    public OrganizationMemberInsurancesDTO() {
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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
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
