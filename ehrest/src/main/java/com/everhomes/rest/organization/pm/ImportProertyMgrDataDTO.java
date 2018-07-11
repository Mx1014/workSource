// @formatter:off
package com.everhomes.rest.organization.pm;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>contactName: 姓名</li>
 * <li>orgOwnerType: 客户类型</li>
 * <li>contactToken: 手机号码</li>
 * <li>gender: 性别</li>
 * <li>building: 楼栋</li>
 * <li>apartment: 门牌</li>
 * <li>livingStatus: 否在户</li>
 * <li>createTime: 迁入日期</li>
 * <li>birthdayDate: 生日</li>
 * <li>maritalStatus: 婚姻状况</li>
 * <li>job:职业</li>
 * <li>company: 单位</li>
 * <li>idCardNumber:证件号码</li>
 * <li>registeredResidence: 户口所在地</li>
 * </ul>
 */
public class ImportProertyMgrDataDTO {
	private String contactName;
	private String orgOwnerType;
	private String contactToken;
	private String gender;
	private String building;
	private String apartment;
	private String livingStatus;
	private String createTime;
	private String birthdayDate;
	private String maritalStatus;
	private String job;
	private String company;
	private String idCardNumber;
	private String registeredResidence;
	
	@ItemType(OrganizationOwnerAddressCommand.class)
    private List<OrganizationOwnerAddressCommand> addresses;
	

	public List<OrganizationOwnerAddressCommand> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<OrganizationOwnerAddressCommand> addresses) {
		this.addresses = addresses;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getOrgOwnerType() {
		return orgOwnerType;
	}

	public void setOrgOwnerType(String orgOwnerType) {
		this.orgOwnerType = orgOwnerType;
	}

	public String getContactToken() {
		return contactToken;
	}

	public void setContactToken(String contactToken) {
		this.contactToken = contactToken;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public String getApartment() {
		return apartment;
	}

	public void setApartment(String apartment) {
		this.apartment = apartment;
	}

	public String getLivingStatus() {
		return livingStatus;
	}

	public void setLivingStatus(String livingStatus) {
		this.livingStatus = livingStatus;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getBirthdayDate() {
		return birthdayDate;
	}

	public void setBirthdayDate(String birthdayDate) {
		this.birthdayDate = birthdayDate;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getIdCardNumber() {
		return idCardNumber;
	}

	public void setIdCardNumber(String idCardNumber) {
		this.idCardNumber = idCardNumber;
	}

	public String getRegisteredResidence() {
		return registeredResidence;
	}

	public void setRegisteredResidence(String registeredResidence) {
		this.registeredResidence = registeredResidence;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
