package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * Created by Ryan on 2017/5/19.
 * <ul>
 * <li>memberId: 员工编号</li>
 * <li>employeeStatus: 员工状态</li>
 * </ul>
 */
public class UpdateOrganizationEmployeeStatusCommand {

    private Long memberId;

    private Byte employeeStatus;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Byte getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(Byte employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    public UpdateOrganizationEmployeeStatusCommand() {
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
