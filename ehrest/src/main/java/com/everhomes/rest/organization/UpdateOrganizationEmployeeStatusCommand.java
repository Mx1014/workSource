package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>detailId: 员工标识号</li>
 * <li>employeeStatus：员工状态, 0: 试用 1: 在职 2: 离职 参考{@link com.everhomes.rest.organization.EmployeeStatus}</li>
 * </ul>
 */
public class UpdateOrganizationEmployeeStatusCommand {

    private Long detailId;

    private Byte employeeStatus;

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
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
