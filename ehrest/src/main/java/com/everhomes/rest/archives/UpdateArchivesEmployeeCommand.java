package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.util.StringHelper;

import java.sql.Date;
import java.util.List;

/**
 * <ul>
 * <li>formOriginId: 表单 id</li>
 * <li>detailId: 员工 id</li>
 * <li>contactName: contactName</li>
 * <li>enName: enName</li>
 * <li>gender: gender</li>
 * <li>birthday: birthday</li>
 * <li>maritalFlag: maritalFlag</li>
 * <li>procreative: procreative</li>
 * <li>ethnicity: ethnicity</li>
 * <li>politicalFlag: politicalFlag</li>
 * <li>nativePlace: nativePlace</li>
 * <li>idType: idType</li>
 * <li>idNumber: idNumber</li>
 * <li>idExpiryDate: idExpiryDate</li>
 * <li>degree: degree</li>
 * <li>graduationSchool: graduationSchool</li>
 * <li>graduationTime: graduationTime</li>
 * <li>contactToken: contactToken</li>
 * <li>email: email</li>
 * <li>wechat: wechat</li>
 * <li>qq: qq</li>
 * <li>address: address</li>
 * <li>emergencyName: emergencyName</li>
 * <li>emergencyRelationship: emergencyRelationship</li>
 * <li>emergencyContact: emergencyContact</li>
 * <li>checkInTime: checkInTime</li>
 * <li>employeeType: employeeType</li>
 * <li>employeeStatus: employeeStatus</li>
 * <li>employmentTime: employmentTime</li>
 * <li>department: department</li>
 * <li>jobPosition: jobPosition</li>
 * <li>reportTarget: reportTarget</li>
 * <li>employeeNo: employeeNo</li>
 * <li>contactShortToken: contactShortToken</li>
 * <li>workEmail: workEmail</li>
 * <li>contractId: contractId</li>
 * <li>workStartTime: workStartTime</li>
 * <li>contractStartTime: contractStartTime</li>
 * <li>contractEndTime: contractEndTime</li>
 * <li>salaryCardNumber: salaryCardNumber</li>
 * <li>salaryCardBank: salaryCardBank</li>
 * <li>socialSecurityNumber: socialSecurityNumber</li>
 * <li>providentFundNumber: providentFundNumber</li>
 * <li>regResidenceType: regResidenceType</li>
 * <li>regResidence: regResidence</li>
 * <li>idPhoto: idPhoto</li>
 * <li>visaPhoto: visaPhoto</li>
 * <li>lifePhoto: lifePhoto</li>
 * <li>entryForm: entryForm</li>
 * <li>graduationCertificate: graduationCertificate</li>
 * <li>degreeCertificate: degreeCertificate</li>
 * <li>contractCertificate: contractCertificate</li>
 * <li>values: 更新字段及其值 {@link com.everhomes.rest.general_approval.PostApprovalFormItem}</li>
 * </ul>
 */
public class UpdateArchivesEmployeeCommand {

    private Long formOriginId;

    private Long detailId;

    private String contactName;

    private String enName;

    private Byte gender;

    private Date birthday;

    private Byte maritalFlag;

    private Date procreative;

    private String ethnicity;

    private String politicalFlag;

    private String nativePlace;

    private Byte idType;

    private String idNumber;

    private Date idExpiryDate;

    private String degree;

    private String graduationSchool;

    private Date graduationTime;

    private String contactToken;

    private String email;

    private String wechat;

    private String qq;

    private String address;

    private String emergencyName;

    private String emergencyRelationship;

    private String emergencyContact;

    private Date checkInTime;

    private Byte employeeType;

    private Byte employeeStatus;

    private Date employmentTime;

    private String department;

    private String jobPosition;

    private String reportTarget;

    private String employeeNo;

    private String contactShortToken;

    private String workEmail;

    private Long contractId;

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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = ArchivesDateUtil.parseDate(birthday);
    }

    public Byte getMaritalFlag() {
        return maritalFlag;
    }

    public void setMaritalFlag(Byte maritalFlag) {
        this.maritalFlag = maritalFlag;
    }

    public Date getProcreative() {
        return procreative;
    }

    public void setProcreative(String procreative) {
        this.procreative = ArchivesDateUtil.parseDate(procreative);
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

    public Byte getIdType() {
        return idType;
    }

    public void setIdType(Byte idType) {
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
        this.idExpiryDate = ArchivesDateUtil.parseDate(idExpiryDate);
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
        this.graduationTime = ArchivesDateUtil.parseDate(graduationTime);
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
        this.employmentTime = ArchivesDateUtil.parseDate(employmentTime);
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

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Date getWorkStartTime() {
        return workStartTime;
    }

    public void setWorkStartTime(String workStartTime) {
        this.workStartTime = ArchivesDateUtil.parseDate(workStartTime);
    }

    public Date getContractStartTime() {
        return contractStartTime;
    }

    public void setContractStartTime(String contractStartTime) {
        this.contractStartTime = ArchivesDateUtil.parseDate(contractStartTime);
    }

    public Date getContractEndTime() {
        return contractEndTime;
    }

    public void setContractEndTime(String contractEndTime) {
        this.contractEndTime = ArchivesDateUtil.parseDate(contractEndTime);
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
