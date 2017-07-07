package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *<li>id: 保险信息标识号</li>
 * <li>name: 保险名称</li>
 * <li>enterprise: 保险公司名称</li>
 * <li>number: 保险编号</li>
 * <li>startTime: 生效时间</li>
 * <li>endTime: 到期时间</li>
 * </ul>
 */
public class UpdateOrganizationMemberInsurancesCommand {

    private Long id;

    private String name;

    private String enterprise;

    private String number;

    private String startTime;

    private String endTime;

    public UpdateOrganizationMemberInsurancesCommand() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
