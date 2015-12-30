package com.everhomes.rest.cooperation;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 主键，传过来的时候没有</li>
 * <li>cooperationType: 前端传过来的机构类型，参考
 * {@link com.everhomes.rest.organization.OrganizationType} 但不完全一样，允许前端传入新类型</li>
 * <li>provinceName: 省</li>
 * <li>cityName:市</li>
 * <li>areaName:区县</li>
 * <li>communityNames:小区名，多个小区用,间隔</li>
 * <li>address:机构地址</li>
 * <li>name:机构名称</li>
 * <li>contactType:联系方式类型，这里固定为0：电话</li>
 * <li>contactToken:机构联系方式</li>
 * <li>applicantName:申请人姓名</li>
 * <li>applicantOccupation:申请人职业</li>
 * <li>applicantPhone:申请人电话</li>
 * <li>applicantEmail:申请人邮箱</li>
 * </ul>
 */

public class NewCooperationCommand { 
	private java.lang.Long   id;

	@NotNull
	private java.lang.String cooperationType;
	private java.lang.String provinceName;
	private java.lang.String cityName;
	private java.lang.String areaName;
	private java.lang.String communityNames;
	private java.lang.String address;
	private java.lang.String name;
	private java.lang.Byte   contactType ;
	private java.lang.String contactToken;
	private java.lang.String applicantName;
	private java.lang.String applicantOccupation;
	private java.lang.String applicantPhone;
	private java.lang.String applicantEmail;

	public NewCooperationCommand() { 
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
 

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
 
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
 
 
	public String getApplicantName() {
		return applicantName;
	}

	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}
 

	public String getApplicantPhone() {
		return applicantPhone;
	}

	public void setApplicantPhone(String applicantPhone) {
		this.applicantPhone = applicantPhone;
	}
 

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public java.lang.String getCommunityNames() {
		return communityNames;
	}

	public void setCommunityNames(java.lang.String communityNames) {
		this.communityNames = communityNames;
	}

	public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	public java.lang.Byte getContactType() {
		return contactType;
	}

	public void setContactType(java.lang.Byte contactType) {
		this.contactType = contactType;
	}

	public java.lang.String getContactToken() {
		return contactToken;
	}

	public void setContactToken(java.lang.String contactToken) {
		this.contactToken = contactToken;
	}

	public java.lang.String getApplicantOccupation() {
		return applicantOccupation;
	}

	public void setApplicantOccupation(java.lang.String applicantOccupation) {
		this.applicantOccupation = applicantOccupation;
	}

	public java.lang.String getApplicantEmail() {
		return applicantEmail;
	}

	public void setApplicantEmail(java.lang.String applicantEmail) {
		this.applicantEmail = applicantEmail;
	}

	public java.lang.String getCooperationType() {
		return cooperationType;
	}

	public void setCooperationType(java.lang.String cooperationType) {
		this.cooperationType = cooperationType;
	}

}
