package com.everhomes.rest.techpark.rental;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 添加场所
 * <li>ownerType：所有者类型 参考{@link com.everhomes.rest.techpark.rental.RentalOwnerType}}</li>
 * <li>ownerId：园区id</li>
 * <li>siteType：场所类型</li>
 * <li>siteName：场所名称</li>
 * <li>buildingName：楼栋名称</li>
 * <li>address：位置</li>
 * <li>spec：用户设置座位数等</li>
 * <li>company：所属公司名</li>
 * <li>contactName：联系人</li>
 * <li>contactPhonenum：电话号码</li>
 * <li>siteItems：场所商品json字符串</li>
 * <li>notice：须知</li>
 * <li>introduction：简介</li>
 * </ul>
 */
public class AddRentalSiteCommand {

	@NotNull
	private String ownerType;
	@NotNull
	private Long ownerId;
	@NotNull
	private String siteType;
	@NotNull
	private String siteName;
	private String buildingName;
	private String address;
	private String spec;
	private String company;
	private String contactName;
	private String contactPhonenum;
	private String introduction;
	private String notice;
	private String siteItems;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
 

	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}
 

	public String getContactPhonenum() {
		return contactPhonenum;
	}

	public void setContactPhonenum(String contactPhonenum) {
		this.contactPhonenum = contactPhonenum;
	}

	public String getSiteItems() {
		return siteItems;
	}

	public void setSiteItems(String siteItems) {
		this.siteItems = siteItems;
	}
 

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}


	public String getOwnerType() {
		return ownerType;
	}


	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}


	public Long getOwnerId() {
		return ownerId;
	}


	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}


	public String getIntroduction() {
		return introduction;
	}


	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}


	public String getNotice() {
		return notice;
	}


	public void setNotice(String notice) {
		this.notice = notice;
	}

	 
}
