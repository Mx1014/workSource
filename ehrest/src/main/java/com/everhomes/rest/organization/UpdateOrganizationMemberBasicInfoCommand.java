package com.everhomes.rest.organization;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>memberId：员工编号</li>
 * <li>contactName：成员名称</li>
 * <li>organizationId: 小区id</li>
 * <li>gender: 成员性别，0：保密 1：男性 2：女性 参考{@link com.everhomes.rest.user.UserGender}</li>
 * <li>departmentsId：部门</li>
 * <li>jobPositionsId: 岗位</li>
 * <li>jobLevelsId: 职级</li>
 * <li>employeeType: 员工类型：0，全职 1，兼职 2，实习 3，劳动派遣 参考{@link com.everhomes.rest.organization.EmployeeType}</li>
 * <li>checkInTime：入职时间</li>
 * <li>contactType：成员类型：{@link com.everhomes.use.IdentifierType}</li>
 * <li>contactToken：联系号码</li>
 * <li>enName：英文名</li>
 * <li>birthday: 出生日期</li>
 * <li>maritalFlag: 婚姻状况：0，保密 1，已婚 2，未婚 参考{@link com.everhomes.rest.organization.MaritalFlag}</li>
 * <li>politicalStatus: 政治面貌</li>
 * <li>nativePlace: 籍贯</li>
 * <li>regResidence: 户口</li>
 * <li>idNumber: 身份证号</li>
 * <li>email: 邮箱</li>
 * <li>weChat: 微信号码</li>
 * <li>qq: QQ号码</li>
 * <li>emergencyName: 紧急联系人姓名</li>
 * <li>emergencyContact: 紧急联系人号码</li>
 * <li>address: 住址</li>
 * <li>memberId: 员工编号</li>
 * <li>salaryCardNumber: 工资卡号</li>
 * <li>socialSecurityNumber: 公积金卡号</li>
 * <li>providentFundNumber: 社保卡号</li>
 * </ul>
 */

public class UpdateOrganizationMemberBasicInfoCommand {

    private Long memberId;

    private String contactName;

    private Byte gender;

    @ItemType(Long.class)
    private List<Long> departmentsId;

    @ItemType(Long.class)
    private List<Long> jobPositionsId;

    @ItemType(Long.class)
    private List<Long> jobLevelsId;

    private Byte employeeType;

    private String checkInTime;

    private Byte contactType;
    private String contactToken;

    private String avatar;

    private String enName;

    private String birthday;

    private Byte maritalFlag;

    private String politicalStatus;

    private String nativePlace;

    private String regResidence;

    private String idNumber;

    private String email;

    private String weChat;

    private String qq;

    private String emergencyName;

    private String emergencyContact;

    private String address;

    private String salaryCardNumber;

    private String socialSecurityNumber;

    private String providentFundNumber;

    public UpdateOrganizationMemberBasicInfoCommand() {
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public List<Long> getDepartmentsId() {
        return departmentsId;
    }

    public void setDepartmentsId(List<Long> departmentsId) {
        this.departmentsId = departmentsId;
    }

    public List<Long> getJobPositionsId() {
        return jobPositionsId;
    }

    public void setJobPositionsId(List<Long> jobPositionsId) {
        this.jobPositionsId = jobPositionsId;
    }

    public List<Long> getJobLevelsId() {
        return jobLevelsId;
    }

    public void setJobLevelsId(List<Long> jobLevelsId) {
        this.jobLevelsId = jobLevelsId;
    }

    public Byte getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(Byte employeeType) {
        this.employeeType = employeeType;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public Byte getContactType() {
        return contactType;
    }

    public void setContactType(Byte contactType) {
        this.contactType = contactType;
    }

    public String getContactToken() {
        return contactToken;
    }

    public void setContactToken(String contactToken) {
        this.contactToken = contactToken;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Byte getMaritalFlag() {
        return maritalFlag;
    }

    public void setMaritalFlag(Byte maritalFlag) {
        this.maritalFlag = maritalFlag;
    }

    public String getPoliticalStatus() {
        return politicalStatus;
    }

    public void setPoliticalStatus(String politicalStatus) {
        this.politicalStatus = politicalStatus;
    }

    public String getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    public String getRegResidence() {
        return regResidence;
    }

    public void setRegResidence(String regResidence) {
        this.regResidence = regResidence;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWeChat() {
        return weChat;
    }

    public void setWeChat(String weChat) {
        this.weChat = weChat;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getEmergencyName() {
        return emergencyName;
    }

    public void setEmergencyName(String emergencyName) {
        this.emergencyName = emergencyName;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSalaryCardNumber() {
        return salaryCardNumber;
    }

    public void setSalaryCardNumber(String salaryCardNumber) {
        this.salaryCardNumber = salaryCardNumber;
    }

    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    public void setSocialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    public String getProvidentFundNumber() {
        return providentFundNumber;
    }

    public void setProvidentFundNumber(String providentFundNumber) {
        this.providentFundNumber = providentFundNumber;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


}
