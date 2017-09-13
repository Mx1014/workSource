package com.everhomes.rest.archives;

import com.everhomes.util.StringHelper;

import java.sql.Date;


/**
 * <ul>
 * <li>organizationId: 公司 id</li>
 * <li>contactName: 员工姓名</li>
 * <li>enName: 英文名</li>
 * <li>gender: 性别: 1-男, 2-女</li>
 * <li>checkInTime: 入职日期</li>
 * <li>employeeType: 员工类型：0，全职 1，兼职 2，实习 3，劳动派遣 {@link com.everhomes.rest.organization.EmployeeType}</li>
 * <li>employeeStatus: 工状态, 0: 试用 1: 在职 2: 离职 {@link com.everhomes.rest.organization.EmployeeStatus}</li>
 * <li>employmentTime: 转正时间</li>
 * <li>departmentId: 部门 id</li>
 * <li>jobPosition: jobPosition</li>
 * <li>employeeNo: 工号</li>
 * <li>contactShortToken: 手机短号</li>
 * <li>workEmail: 工作邮箱</li>
 * <li>contractId: 合同主体</li>
 * <li>regionCode: 手机区号</li>
 * <li>contactToken: 手机号</li>
 * </ul>
 */
public class AddArchivesEmployeeCommand {

    private Long organizationId;

    private String contactName;

    private String enName;

    private Byte gender;

    private Date checkInTime;

    private Byte employeeType;

    private Byte employeeStatus;

    private Date employmentTime;

    private Long departmentId;

    private String jobPosition;

    private String employeeNo;

    private String contactShortToken;

    private String workEmail;

    private Long contractId;

    private String regionCode;

    private String contactToken;

    public AddArchivesEmployeeCommand() {
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public Date getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = ArchivesDateUtil.parseDate(checkInTime);
    }

    public Byte getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(Byte employeeType) {
        this.employeeType = employeeType;
    }

    public Byte getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(Byte employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    public Date getEmploymentTime() {
        return employmentTime;
    }

    public void setEmploymentTime(String employmentTime) {
        this.employmentTime =  ArchivesDateUtil.parseDate(employmentTime);
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
    }

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    public String getContactShortToken() {
        return contactShortToken;
    }

    public void setContactShortToken(String contactShortToken) {
        this.contactShortToken = contactShortToken;
    }

    public String getWorkEmail() {
        return workEmail;
    }

    public void setWorkEmail(String workEmail) {
        this.workEmail = workEmail;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getContactToken() {
        return contactToken;
    }

    public void setContactToken(String contactToken) {
        this.contactToken = contactToken;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
