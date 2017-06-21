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
 * <li>departments: 员工部门 {@link com.everhomes.rest.organization.OrganizationDTO}</li>
 * <li>jobPositions: 员工岗位 {@link com.everhomes.rest.organization.OrganizationDTO}</li>
 * <li>salaryGroupId: 批次id</li>
 * <li>salaryGroupName: 批次名称</li>
 * <li>isConfirmed: 工资明细:0-已设置 1-未设置</li>
 * </ul>
 */
public class salaryEmployeeDTO {

    private Long userId;

    private String employeeNo;

    private String contactName;

    @ItemType(OrganizationDTO.class)
    private List<OrganizationDTO> departments;

    @ItemType(OrganizationDTO.class)
    private List<OrganizationDTO> jobPositions;

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

    public List<OrganizationDTO> getDepartments() {
        return departments;
    }

    public void setDepartments(List<OrganizationDTO> departments) {
        this.departments = departments;
    }

    public List<OrganizationDTO> getJobPositions() {
        return jobPositions;
    }

    public void setJobPositions(List<OrganizationDTO> jobPositions) {
        this.jobPositions = jobPositions;
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
