package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>detailId: 成员 detailId</li>
 * <li>contactName: 姓名</li>
 * <li>employeeStatus: 成员状态: 0-试用, 1-正式</li>
 * <li>jobPositions: 职务 {@link OrganizationDTO}</li>
 * <li>departments: 部门 {@link OrganizationDTO}</li>
 * <li>contactToken: 手机号</li>
 * <li>email: 邮箱</li>
 * <li>employmentTime: 转正日期</li>
 * <li>contractTime: 合同日期</li>
 * </ul>
 */
public class ArchivesEmployeeDTO {

    private Long detailId;

    private String contactName;

    private String employeeStatus;

    @ItemType(OrganizationDTO.class)
    private List<OrganizationDTO> jobPositions;

    @ItemType(OrganizationDTO.class)
    private List<OrganizationDTO> departments;

    private String contactToken;

    private String email;

    private String employmentTime;

    private String contractTime;

    public ArchivesEmployeeDTO() {
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(String employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    public List<OrganizationDTO> getJobPositions() {
        return jobPositions;
    }

    public void setJobPositions(List<OrganizationDTO> jobPositions) {
        this.jobPositions = jobPositions;
    }

    public List<OrganizationDTO> getDepartments() {
        return departments;
    }

    public void setDepartments(List<OrganizationDTO> departments) {
        this.departments = departments;
    }

    public String getContactToken() {
        return contactToken;
    }

    public void setContactToken(String contactToken) {
        this.contactToken = contactToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmploymentTime() {
        return employmentTime;
    }

    public void setEmploymentTime(String employmentTime) {
        this.employmentTime = employmentTime;
    }

    public String getContractTime() {
        return contractTime;
    }

    public void setContractTime(String contractTime) {
        this.contractTime = contractTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
