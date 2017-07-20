package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

import java.sql.Date;

/**
 * <ul>
 * <li>detailId: 员工标识号</li>
 * <li>employeeStatus：新增员工状态, 0: 试用 1: 在职 2: 离职 参考{@link com.everhomes.rest.organization.EmployeeStatus}</li>
 * <li>date: 日期</li>
 * <li>remarks: 备注</li>
 * <li>personChangeType: 操作类型，0: 入职 1: 转正 2: 部门变动 3: 岗位变动 4: 职级变动 5: 离职 6: 设置试用 参考{@link com.everhomes.rest.organization.PersonChangeType}</li>
 * </ul>
 */
public class UpdateOrganizationEmployeeStatusCommand {

    private Long detailId;

    private Byte employeeStatus;

    private String date;

    private String remarks;

    private String personChangeType;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPersonChangeType() {
        return personChangeType;
    }

    public void setPersonChangeType(String personChangeType) {
        this.personChangeType = personChangeType;
    }

    public UpdateOrganizationEmployeeStatusCommand() {
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
