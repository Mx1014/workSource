package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.util.StringHelper;

import java.sql.Date;
import java.util.List;

/**
 * <ul>
 * <li>values: (List)被修改的字段及值 {@link com.everhomes.rest.general_approval.PostApprovalFormItem}</li>
 * </ul>
 */
public class UpdateArchivesEmployeeCommand {

    private Long formOriginId;

    private Long detailId;

    private Long organizationId;

    private String contactName;

    private String enName;

    private String gender;

    private Date birthday;

    private String maritalFlag;

    private Date procreative;

    private String ethnicity;

    private String politicalFlag;

    private String nativePlace;

    private String idType;

    private String idNumber;

    private Date idExpiryDate;

    private String degree;

    private String graduationSchool;

    private Date graduationTime;

    private String regionCode;

    private String contactToken;

    private String email;

    private String wechat;

    private String qq;

    private String address;

    private String emergencyName;

    private String emergencyRelationship;

    private String emergencyContact;

    private Date checkInTime;

    private String employeeType;

    private String employeeStatus;

    private Date employmentTime;

    private String department;

    private String jobPosition;

    private String reportTarget;

    private String employeeNo;

    private String contactShortToken;

    private String workEmail;

    private Long contractPartyId;

    private Date workStartTime;

    private Date contractStartTime;

    private Date contractEndTime;

    private String salaryCardNumber;

    private String salaryCardBank;

    private String socialSecurityNumber;

    private String providentFundNumber;

    private String regResidenceType;

    private String regResidence;

    private String idPhoto;

    private String visaPhoto;

    private String lifePhoto;

    private String entryForm;

    private String graduationCertificate;

    private String degreeCertificate;

    private String contractCertificate;

    @ItemType(PostApprovalFormItem.class)
    List<PostApprovalFormItem> values;

    public UpdateArchivesEmployeeCommand() {
    }

    public Long getFormOriginId() {
        return formOriginId;
    }

    public void setFormOriginId(Long formOriginId) {
        this.formOriginId = formOriginId;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = ArchivesUtil.parseDate(birthday);
    }

    public String getMaritalFlag() {
        return maritalFlag;
    }

    public void setMaritalFlag(String maritalFlag) {
        this.maritalFlag = maritalFlag;
    }

    public Date getProcreative() {
        return procreative;
    }

    public void setProcreative(String procreative) {
        this.procreative = ArchivesUtil.parseDate(procreative);
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    public String getPoliticalFlag() {
        return politicalFlag;
    }

    public void setPoliticalFlag(String politicalFlag) {
        this.politicalFlag = politicalFlag;
    }

    public String getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public Date getIdExpiryDate() {
        return idExpiryDate;
    }

    public void setIdExpiryDate(String idExpiryDate) {
        this.idExpiryDate = ArchivesUtil.parseDate(idExpiryDate);
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getGraduationSchool() {
        return graduationSchool;
    }

    public void setGraduationSchool(String graduationSchool) {
        this.graduationSchool = graduationSchool;
    }

    public Date getGraduationTime() {
        return graduationTime;
    }

    public void setGraduationTime(String graduationTime) {
        this.graduationTime = ArchivesUtil.parseDate(graduationTime);
    }

    public String getContactToken() {
        return contactToken;
    }

    public void setContactToken(String contactToken) {
        this.contactToken = contactToken;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmergencyName() {
        return emergencyName;
    }

    public void setEmergencyName(String emergencyName) {
        this.emergencyName = emergencyName;
    }

    public String getEmergencyRelationship() {
        return emergencyRelationship;
    }

    public void setEmergencyRelationship(String emergencyRelationship) {
        this.emergencyRelationship = emergencyRelationship;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public Date getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = ArchivesUtil.parseDate(checkInTime);
    }

    public String getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }

    public String getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(String employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    public Date getEmploymentTime() {
        return employmentTime;
    }

    public void setEmploymentTime(String employmentTime) {
        this.employmentTime = ArchivesUtil.parseDate(employmentTime);
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
    }

    public String getReportTarget() {
        return reportTarget;
    }

    public void setReportTarget(String reportTarget) {
        this.reportTarget = reportTarget;
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

    public Date getWorkStartTime() {
        return workStartTime;
    }

    public void setWorkStartTime(String workStartTime) {
        this.workStartTime = ArchivesUtil.parseDate(workStartTime);
    }

    public Date getContractStartTime() {
        return contractStartTime;
    }

    public void setContractStartTime(String contractStartTime) {
        this.contractStartTime = ArchivesUtil.parseDate(contractStartTime);
    }

    public Date getContractEndTime() {
        return contractEndTime;
    }

    public void setContractEndTime(String contractEndTime) {
        this.contractEndTime = ArchivesUtil.parseDate(contractEndTime);
    }

    public String getSalaryCardNumber() {
        return salaryCardNumber;
    }

    public void setSalaryCardNumber(String salaryCardNumber) {
        this.salaryCardNumber = salaryCardNumber;
    }

    public String getSalaryCardBank() {
        return salaryCardBank;
    }

    public void setSalaryCardBank(String salaryCardBank) {
        this.salaryCardBank = salaryCardBank;
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

    public String getRegResidenceType() {
        return regResidenceType;
    }

    public void setRegResidenceType(String regResidenceType) {
        this.regResidenceType = regResidenceType;
    }

    public String getRegResidence() {
        return regResidence;
    }

    public void setRegResidence(String regResidence) {
        this.regResidence = regResidence;
    }

    public String getIdPhoto() {
        return idPhoto;
    }

    public void setIdPhoto(String idPhoto) {
        this.idPhoto = idPhoto;
    }

    public String getVisaPhoto() {
        return visaPhoto;
    }

    public void setVisaPhoto(String visaPhoto) {
        this.visaPhoto = visaPhoto;
    }

    public String getLifePhoto() {
        return lifePhoto;
    }

    public void setLifePhoto(String lifePhoto) {
        this.lifePhoto = lifePhoto;
    }

    public String getEntryForm() {
        return entryForm;
    }

    public void setEntryForm(String entryForm) {
        this.entryForm = entryForm;
    }

    public String getGraduationCertificate() {
        return graduationCertificate;
    }

    public void setGraduationCertificate(String graduationCertificate) {
        this.graduationCertificate = graduationCertificate;
    }

    public String getDegreeCertificate() {
        return degreeCertificate;
    }

    public void setDegreeCertificate(String degreeCertificate) {
        this.degreeCertificate = degreeCertificate;
    }

    public String getContractCertificate() {
        return contractCertificate;
    }

    public void setContractCertificate(String contractCertificate) {
        this.contractCertificate = contractCertificate;
    }

    public List<PostApprovalFormItem> getValues() {
        return values;
    }

    public void setValues(List<PostApprovalFormItem> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
