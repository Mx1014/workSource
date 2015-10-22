package com.everhomes.techpark.rental;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 更新预定规则
 * <li>enterpriseCommunityId：园区id</li>
 * <li>siteType：场所类型</li>
 * <li>siteName：场所名称</li>
 * <li>buildingName：楼栋名称</li>
 * <li>address：位置</li>
 * <li>spec：用户设置座位数等</li>
 * <li>company：所属公司名</li>
 * <li>contactName：联系人</li>
 * <li>contactPhonenum：电话号码</li>
 * <li>siteItems：场所商品json字符串</li>
 * </ul>
 */
public class AddRentalSiteCommand {
	private Long enterpriseCommunityId;
	private String siteType;
	private String siteName;
	private String buildingName;
	private String address;
	private String spec;
	private String company;
	private String contactName;
	private String contactPhonenum;
	private String siteItems;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getEnterpriseCommunityId() {
		return enterpriseCommunityId;
	}

	public void setEnterpriseCommunityId(Long enterpriseCommunityId) {
		this.enterpriseCommunityId = enterpriseCommunityId;
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
}
