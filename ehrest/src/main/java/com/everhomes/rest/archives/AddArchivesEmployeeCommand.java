package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;


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
 * <li>month: 试用月份</li>
 * <li>departmentIds: 部门 ids</li>
 * <li>jobPositionIds: 岗位 ids</li>
 * <li>jobLevelIds: 职级 ids</li>
 * <li>employeeNo: 工号</li>
 * <li>contactShortToken: 手机短号</li>
 * <li>workEmail: 工作邮箱</li>
 * <li>contractPartyId: 合同主体</li>
 * <li>regionCode: 手机区号</li>
 * <li>contactToken: 手机号</li>
 * <li>socialSecurityStartMonth: 社保增员月 0-本月 1-次月 参考{@link com.everhomes.rest.archives.SocialSecurityMonthType}</li>
 * <li>accumulationFundStartMonth: 公积金增员月 0-本月 1-次月 参考{@link com.everhomes.rest.archives.SocialSecurityMonthType}</li>
 * </ul>
 */
public class AddArchivesEmployeeCommand {

    private Long organizationId;

    private String contactName;

    private String enName;

    private Byte gender;

    private String checkInTime;

    private Byte employeeType;

    private Byte employeeStatus;

    private String employmentTime;

    private Integer month;

    @ItemType(Long.class)
    private List<Long> departmentIds;

    @ItemType(Long.class)
    private List<Long> jobPositionIds;

    @ItemType(Long.class)
    private List<Long> jobLevelIds;

    private String employeeNo;

    private String contactShortToken;

    private String workEmail;

    private Long contractPartyId;

    private String regionCode;

    private String contactToken;

    //  added by Rong for social security at 01.05-2018
    private Byte socialSecurityStartMonth;

    private Byte accumulationFundStartMonth;

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

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public List<Long> getDepartmentIds() {
        return departmentIds;
    }

    public void setDepartmentIds(List<Long> departmentIds) {
        this.departmentIds = departmentIds;
    }

    public List<Long> getJobPositionIds() {
        return jobPositionIds;
    }

    public void setJobPositionIds(List<Long> jobPositionIds) {
        this.jobPositionIds = jobPositionIds;
    }

    public List<Long> getJobLevelIds() {
        return jobLevelIds;
    }

    public void setJobLevelIds(List<Long> jobLevelIds) {
        this.jobLevelIds = jobLevelIds;
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

    public Long getContractPartyId() {
        return contractPartyId;
    }

    public void setContractPartyId(Long contractPartyId) {
        this.contractPartyId = contractPartyId;
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

    public Byte getSocialSecurityStartMonth() {
        return socialSecurityStartMonth;
    }

    public void setSocialSecurityStartMonth(Byte socialSecurityStartMonth) {
        this.socialSecurityStartMonth = socialSecurityStartMonth;
    }

    public Byte getAccumulationFundStartMonth() {
        return accumulationFundStartMonth;
    }

    public void setAccumulationFundStartMonth(Byte accumulationFundStartMonth) {
        this.accumulationFundStartMonth = accumulationFundStartMonth;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
