package com.everhomes.rest.archives;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>organizationId: 公司 id</li>
 * <li>contactName: 员工姓名</li>
 * <li>checkInTime: 入职日期</li>
 * <li>employeeType: 员工类型：0，全职 1，兼职 2，实习 3，劳动派遣</li>
 * <li>employeeStatus: 工状态, 0: 试用 1: 在职 2: 离职 </li>
 * <li>employmentTime: 转正时间</li>
 * <li>departmentId: 部门 id</li>
 * <li>jobPositionId: 职务</li>
 * <li>employeeNo: 工号</li>
 * <li>contactShortToken: 手机短号</li>
 * <li>email: 工作邮箱</li>
 * <li>workingPlace: 工作地点</li>
 * <li>contractId: 合同主体</li>
 * <li>areaCode: 手机区号</li>
 * <li>contactToken: 手机号</li>
 * </ul>
 */
public class AddArchivesEmployeeCommand {

    private Long organizationId;

    private String contactName;

    private String checkInTime;

    private Byte employeeType;

    private Byte employeeStatus;

    private String employmentTime;

    private Long departmentId;

    private String jobPosition;

    private String employeeNo;

    private String contactShortToken;

    private String email;

    private String workingPlace;

    private Long contractId;

    private String areaCode;

    private String contactToken;

    public AddArchivesEmployeeCommand() {
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
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

    public String getEmploymentTime() {
        return employmentTime;
    }

    public void setEmploymentTime(String employmentTime) {
        this.employmentTime = employmentTime;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWorkingPlace() {
        return workingPlace;
    }

    public void setWorkingPlace(String workingPlace) {
        this.workingPlace = workingPlace;
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

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
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
