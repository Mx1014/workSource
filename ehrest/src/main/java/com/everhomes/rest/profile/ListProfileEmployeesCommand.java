package com.everhomes.rest.profile;

import com.everhomes.util.StringHelper;

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

    private String checkInTimeStart;

    private String checkInTimeEnd;

    private String employmentTimeStart;

    private String employmentTimeEnd;

    private String contractTimeStart;

    private String contractTimeEnd;

    private String employeeStatus;

    private String organizationId;

    private String contactName;

    private String departmentId;

    private String workingPlace;

    public ListProfileEmployeesCommand() {
    }

    public String getCheckInTimeStart() {
        return checkInTimeStart;
    }

    public void setCheckInTimeStart(String checkInTimeStart) {
        this.checkInTimeStart = checkInTimeStart;
    }

    public String getCheckInTimeEnd() {
        return checkInTimeEnd;
    }

    public void setCheckInTimeEnd(String checkInTimeEnd) {
        this.checkInTimeEnd = checkInTimeEnd;
    }

    public String getEmploymentTimeStart() {
        return employmentTimeStart;
    }

    public void setEmploymentTimeStart(String employmentTimeStart) {
        this.employmentTimeStart = employmentTimeStart;
    }

    public String getEmploymentTimeEnd() {
        return employmentTimeEnd;
    }

    public void setEmploymentTimeEnd(String employmentTimeEnd) {
        this.employmentTimeEnd = employmentTimeEnd;
    }

    public String getContractTimeStart() {
        return contractTimeStart;
    }

    public void setContractTimeStart(String contractTimeStart) {
        this.contractTimeStart = contractTimeStart;
    }

    public String getContractTimeEnd() {
        return contractTimeEnd;
    }

    public void setContractTimeEnd(String contractTimeEnd) {
        this.contractTimeEnd = contractTimeEnd;
    }

    public String getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(String employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
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
