// @formatter:off
package com.everhomes.organization;

import com.everhomes.server.schema.tables.pojos.EhOrganizationMembers;
import com.everhomes.util.StringHelper;

import java.sql.Date;
import java.sql.Timestamp;

public class OrganizationMember extends EhOrganizationMembers implements Comparable<OrganizationMember> {

    private static final long serialVersionUID = -4420904659870582839L;

    private java.lang.String nickName;

    private java.lang.String organizationName;

    private String initial;

    private String fullPinyin;

    private String fullInitial;

    private Boolean isCreate;

    private Byte employeeStatus;

    private Date employmentTime;

    private Date checkInTime;

	private Byte employeeType;

    private Long enterpriserId;

    private String email;

    private String workEmail;

    private String regionCode;

    private String contactEnName;

    private Date contractEndTime;

    private String contactShortToken;

    //  组织架构4.6 公司唯一标识
    private String account;

    public OrganizationMember() {
    }
    public java.lang.String getNickName() {
        return nickName;
    }

    public void setNickName(java.lang.String nickName) {
        this.nickName = nickName;
    }

    public String getInitial() {
        return initial;
    }

    public Long getApproveTime() {
        return OrganizationMemberCustomField.APPROVE_TIME.getIntegralValue(this);
    }

    public void setApproveTime(Long approveTime) {
        OrganizationMemberCustomField.APPROVE_TIME.setIntegralValue(this, approveTime);
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public String getFullPinyin() {
        return fullPinyin;
    }

    public void setFullPinyin(String fullPinyin) {
        this.fullPinyin = fullPinyin;
    }

    public String getFullInitial() {
        return fullInitial;
    }

    public void setFullInitial(String fullInitial) {
        this.fullInitial = fullInitial;
    }

    public int compareTo(OrganizationMember organizationMember) {
        return this.initial.compareTo(organizationMember.getInitial());
    }

    public Boolean isCreate() {
        return isCreate != null ? isCreate : false;
    }

    public void setCreate(Boolean isCreate) {
        this.isCreate = isCreate;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
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

    public void setEmploymentTime(Date employmentTime) {
        this.employmentTime = employmentTime;
    }

	public Byte getEmployeeType() {
		return employeeType;
	}

	public void setEmployeeType(Byte employeeType) {
		this.employeeType = employeeType;
	}

    public Date getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(Date checkInTime) {
        this.checkInTime = checkInTime;
    }

    public Long getEnterpriserId() {
        return enterpriserId;
    }

    public void setEnterpriserId(Long enterpriserId) {
        this.enterpriserId = enterpriserId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWorkEmail() {
        return workEmail;
    }

    public void setWorkEmail(String workEmail) {
        this.workEmail = workEmail;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getContactEnName() {
        return contactEnName;
    }

    public void setContactEnName(String contactEnName) {
        this.contactEnName = contactEnName;
    }

    public Date getContractEndTime() {
        return contractEndTime;
    }

    public void setContractEndTime(Date contractEndTime) {
        this.contractEndTime = contractEndTime;
    }

    public String getContactShortToken() {
        return contactShortToken;
    }

    public void setContactShortToken(String contactShortToken) {
        this.contactShortToken = contactShortToken;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
