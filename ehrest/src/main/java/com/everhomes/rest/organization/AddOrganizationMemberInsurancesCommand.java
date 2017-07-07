package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>detailId: 员工标识号</li>
 * <li>name: 保险名称</li>
 * <li>enterprise: 保险公司名称</li>
 * <li>number: 保险编号</li>
 * <li>startTime: 生效时间</li>
 * <li>endTime: 到期时间</li>
 * </ul>
 */
public class AddOrganizationMemberInsurancesCommand {

    @NotNull
    private Long detailId;

    @NotNull
    private String name;

    @NotNull
    private String enterprise;

    @NotNull
    private String number;

    @NotNull
    private String startTime;

    @NotNull
    private String endTime;

    public AddOrganizationMemberInsurancesCommand() {
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
