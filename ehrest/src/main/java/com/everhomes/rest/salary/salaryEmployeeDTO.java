package com.everhomes.rest.salary;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>userId: 员工id</li>
 * <li>employeeNo: 员工编号(可为空)</li>
 * <li>contactName: 员工姓名</li>
 * <li>department: 员工部门 </li>
 * <li>jobPosition: 员工岗位 </li>
 * <li>salaryGroupId: 批次id</li>
 * <li>salaryGroupName: 批次名称</li>
 * <li>isConfirmed: 工资明细:0-已设置 1-未设置</li>
 * </ul>
 */
public class salaryEmployeeDTO {

    private Long userId;

    private String employeeNo;

    private String contactName;

/*
    @ItemType(OrganizationDTO.class)
    private List<OrganizationDTO> departments;

    @ItemType(OrganizationDTO.class)
    private List<OrganizationDTO> jobPositions;
*/
    private String department;

    private String jobPosition;

    private Long salaryGroupId;

    private String salaryGroupName;

    private Byte isConfirmed;

    public salaryEmployeeDTO() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
    }

    public Long getSalaryGroupId() {
        return salaryGroupId;
    }

    public void setSalaryGroupId(Long salaryGroupId) {
        this.salaryGroupId = salaryGroupId;
    }

    public String getSalaryGroupName() {
        return salaryGroupName;
    }

    public void setSalaryGroupName(String salaryGroupName) {
        this.salaryGroupName = salaryGroupName;
    }

    public Byte getIsConfirmed() {
        return isConfirmed;
    }

    public void setIsConfirmed(Byte isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
