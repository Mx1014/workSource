package com.everhomes.rest.salary;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>contactName: 员工姓名</li>
 * <li>departments: 员工部门 {@link com.everhomes.rest.organization.OrganizationDTO}</li>
 * <li>jobPositions: 员工岗位 {@link com.everhomes.rest.organization.OrganizationDTO}</li>
 * <li>employeeOriginVal: 员工详细薪酬 {@link com.everhomes.rest.salary.SalaryEmployeeOriginValDTO}</li>
 * </ul>
 */
public class salaryEmployeeDTO {

    private String contactName;

    @ItemType(OrganizationDTO.class)
    private List<OrganizationDTO> departments;

    @ItemType(OrganizationDTO.class)
    private List<OrganizationDTO> jobPositions;

    @ItemType(SalaryEmployeeOriginValDTO.class)
    private List<SalaryEmployeeOriginValDTO> employeeOriginVal;

    public salaryEmployeeDTO() {
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

    public List<SalaryEmployeeOriginValDTO> getEmployeeOriginVal() {
        return employeeOriginVal;
    }

    public void setEmployeeOriginVal(List<SalaryEmployeeOriginValDTO> employeeOriginVal) {
        this.employeeOriginVal = employeeOriginVal;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
