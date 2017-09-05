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
 * <li>jobPositions: 职务</li>
 * <li>departments: 部门 {@link com.everhomes.rest.organization.OrganizationDTO}</li>
 * <li>contactToken: 手机号</li>
 * <li>workEmail: 邮箱</li>
 * <li>employmentTime: 转正日期</li>
 * <li>contractTime: 合同日期</li>
 * </ul>
 */
public class ArchivesEmployeeDTO {

    private Long detailId;

    private Long targetId;

    private String targetType;

    private String contactName;

    private String employeeStatus;

    private String jobPosition;

    @ItemType(OrganizationDTO.class)
    private List<OrganizationDTO> departments;

    private String contactToken;

    private String workEmail;

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

    public String getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
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

    public String getWorkEmail() {
        return workEmail;
    }

    public void setWorkEmail(String workEmail) {
        this.workEmail = workEmail;
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
