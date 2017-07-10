package com.everhomes.rest.organization;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>detailId：员工标识号</li>
 * <li>enName：英文名</li>
 * <li>birthday: 出生日期</li>
 * <li>maritalFlag: 婚姻状况：0，保密 1，已婚 2，未婚 参考{@link com.everhomes.rest.organization.MaritalFlag}</li>
 * <li>politicalStatus: 政治面貌</li>
 * <li>nativePlace: 籍贯</li>
 * <li>regResidence: 户口</li>
 * <li>idNumber: 身份证号</li>
 * <li>email: 邮箱</li>
 * <li>wechat: 微信号码</li>
 * <li>qq: QQ号码</li>
 * <li>emergencyName: 紧急联系人姓名</li>
 * <li>emergencyContact: 紧急联系人号码</li>
 * <li>address: 住址</li>
 * <li>salaryCardNumber: 工资卡号</li>
 * <li>socialSecurityNumber: 公积金卡号</li>
 * <li>providentFundNumber: 社保卡号</li>
 * </ul>
 */

public class UpdateOrganizationMemberBackGroundCommand {

    private Long detailId;

    private String enName;

    private String birthday;

    private Byte maritalFlag;

    private String politicalStatus;

    private String nativePlace;

    private String regResidence;

    private String idNumber;

    private String email;

    private String wechat;

    private String qq;

    private String emergencyName;

    private String emergencyContact;

    private String address;

    private String salaryCardNumber;

    private String socialSecurityNumber;

    private String providentFundNumber;

    public UpdateOrganizationMemberBackGroundCommand() {
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
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

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
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
