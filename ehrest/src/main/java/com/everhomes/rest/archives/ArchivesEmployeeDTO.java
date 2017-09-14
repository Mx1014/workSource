package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.util.StringHelper;

import java.sql.Date;
import java.util.List;

/**
 * <ul>
 * <li>detailId: 成员 detailId</li>
 * <li>contactName: 姓名</li>
 * <li>employeeStatus: 成员状态: 0-试用, 1-正式</li>
 * <li>jobPositions: 职务</li>
 * <li>departments: 部门 {@link com.everhomes.rest.organization.OrganizationDTO}</li>
 * <li>contactToken: 手机号</li>
 * <li>workEmail: 工作邮箱</li>
 * <li>employmentTime: 转正日期</li>
 * <li>contractTime: 合同日期</li>
 * </ul>
 */
public class ArchivesEmployeeDTO {

    private Long detailId;

    private Long targetId;

    private String targetType;

    private String contactName;

    private Byte employeeStatus;

    private String jobPosition;

    @ItemType(OrganizationDTO.class)
    private List<OrganizationDTO> departments;

    private String contactToken;

    private String workEmail;

    private Date checkInTime;

    private Date employmentTime;

    private Date contractTime;

    public ArchivesEmployeeDTO() {
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Byte getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(Byte employeeStatus) {
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

    public Date getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(Date checkInTime) {
        this.checkInTime = checkInTime;
    }

    public Date getEmploymentTime() {
        return employmentTime;
    }

    public void setEmploymentTime(Date employmentTime) {
        this.employmentTime = employmentTime;
    }

    public Date getContractTime() {
        return contractTime;
    }

    public void setContractTime(Date contractTime) {
        this.contractTime = contractTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
