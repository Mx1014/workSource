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
    // private java.lang.String   avatar;

    private String initial;

    private String fullPinyin;
    private String fullInitial;

    private java.lang.Long creatorUid;

    private Boolean isCreate;

    // private String applyDescription;// 申请加入公司时填写的描述信息   add by xq.tian  2017/05/02

    // private Long approveTime;// 审核时间

    private Byte employeeStatus;

    private Date employmentTime;

    private Integer profileIntegrity;

    private Date checkInTime;

	private String employeeNo;

	private Byte employeeType;

    private Long enterpriserId;

    private String email;
    private String workEmail;
    private String regionCode;
    private Date contractEndTime;
    private String contactShortToken;

    public OrganizationMember() {
    }

    public java.lang.String getNickName() {
        return nickName;
    }


    public void setNickName(java.lang.String nickName) {
        this.nickName = nickName;
    }


    public java.lang.Long getCreatorUid() {
        return creatorUid;
    }


    public void setCreatorUid(java.lang.Long creatorUid) {
        this.creatorUid = creatorUid;
    }


    public String getInitial() {
        return initial;
    }


    /*public String getApplyDescription() {
        return OrganizationMemberCustomField.APPLY_DESCRIPTION.getStringValue(this);
    }

    public void setApplyDescription(String applyDescription) {
        OrganizationMemberCustomField.APPLY_DESCRIPTION.setStringValue(this, applyDescription);
    }*/

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

    public Integer getProfileIntegrity() {
        return profileIntegrity;
    }

    public void setProfileIntegrity(Integer profileIntegrity) {
        this.profileIntegrity = profileIntegrity;
    }


	public String getEmployeeNo() {
		return employeeNo;
	}

	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
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
}
