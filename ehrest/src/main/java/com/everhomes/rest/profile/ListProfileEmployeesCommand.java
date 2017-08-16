package com.everhomes.rest.profile;

import com.everhomes.util.StringHelper;

import java.sql.Date;

/**
 * <ul>
 * <li>checkInTimeStart: 入职起始日期</li>
 * <li>checkInTimeEnd: 入职结束日期</li>
 * <li>employmentTimeStart: 转正起始日期</li>
 * <li>employmentTimeEnd: 转正结束日期</li>
 * <li>contractTimeStart: 合同开始日期</li>
 * <li>contractTimeEnd: 合同结束日期</li>
 * <li>employeeStatus: 员工状态</li>
 * <li>organizationId: 合同主体 id</li>
 * <li>contactName: 姓名</li>
 * <li>departmentId: 部门 id</li>
 * <li>workingPlace: 工作地点</li>
 * </ul>
 */
public class ListProfileEmployeesCommand {

    private Date checkInTimeStart;

    private Date checkInTimeEnd;

    private Date employmentTimeStart;

    private Date employmentTimeEnd;

    private Date contractTimeStart;

    private Date contractTimeEnd;

    private Byte employeeStatus;

    private Long organizationId;

    private String contactName;

    private Long departmentId;

    private String workingPlace;

    public ListProfileEmployeesCommand() {
    }

    public Date getCheckInTimeStart() {
        return checkInTimeStart;
    }

    public void setCheckInTimeStart(Date checkInTimeStart) {
        this.checkInTimeStart = checkInTimeStart;
    }

    public Date getCheckInTimeEnd() {
        return checkInTimeEnd;
    }

    public void setCheckInTimeEnd(Date checkInTimeEnd) {
        this.checkInTimeEnd = checkInTimeEnd;
    }

    public Date getEmploymentTimeStart() {
        return employmentTimeStart;
    }

    public void setEmploymentTimeStart(Date employmentTimeStart) {
        this.employmentTimeStart = employmentTimeStart;
    }

    public Date getEmploymentTimeEnd() {
        return employmentTimeEnd;
    }

    public void setEmploymentTimeEnd(Date employmentTimeEnd) {
        this.employmentTimeEnd = employmentTimeEnd;
    }

    public Date getContractTimeStart() {
        return contractTimeStart;
    }

    public void setContractTimeStart(Date contractTimeStart) {
        this.contractTimeStart = contractTimeStart;
    }

    public Date getContractTimeEnd() {
        return contractTimeEnd;
    }

    public void setContractTimeEnd(Date contractTimeEnd) {
        this.contractTimeEnd = contractTimeEnd;
    }

    public Byte getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(Byte employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getWorkingPlace() {
        return workingPlace;
    }

    public void setWorkingPlace(String workingPlace) {
        this.workingPlace = workingPlace;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
