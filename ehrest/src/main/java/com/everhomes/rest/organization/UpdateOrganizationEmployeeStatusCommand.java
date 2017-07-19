package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

import java.sql.Date;

/**
 * <ul>
 * <li>detailId: 员工标识号</li>
 * <li>employeeStatus：新增员工状态, 0: 试用 1: 在职 2: 离职 参考{@link com.everhomes.rest.organization.EmployeeStatus}</li>
 * <li>date: 日期</li>
 * <li>remarks: 备注</li>
 * </ul>
 */
public class UpdateOrganizationEmployeeStatusCommand {

    private Long detailId;

    private Byte employeeStatus;

    private Date date;

    private String remarks;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public UpdateOrganizationEmployeeStatusCommand() {
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
