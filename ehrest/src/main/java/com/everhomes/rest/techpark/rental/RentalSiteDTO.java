package com.everhomes.rest.techpark.rental;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * <ul>
 * 场所
 * <li>rentalSiteId：场所id</li>
 * <li>enterpriseCommunityId：园区id</li>
 * <li>siteType：场所类型</li>
 * <li>siteName：场所名称</li>
 * <li>buildingName：楼栋名称</li>
 * <li>address：位置</li>
 * <li>spec：用户设置座位数等</li>
 * <li>companyId：场所隶属的公司id</li>
 * <li>ownId：负责人id</li>
 * <li>contactPhonenum：电话号码</li>
 * <li>siteItems：场所商品</li>
 * </ul>
 */
public class RentalSiteDTO {
	private Long rentalSiteId;
	private String ownerType;
	private Long ownerId;
	private String siteType;
	private String siteName;
	private String buildingName;
	private String address;
	private String spec;
	private String companyName;
	private String contactName;
	private String contactPhonenum;
	private String introduction;
	private String notice; 
	private java.lang.Byte     status;
	private Long createTime;
	@ItemType(SiteItemDTO.class)
	private List<SiteItemDTO> siteItems;
	@ItemType(RentalSiteRulesDTO.class)
	private List<RentalSiteRulesDTO> siteRules;
	
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

	public List<SiteItemDTO> getSiteItems() {
		return siteItems;
	}

	public void setSiteItems(List<SiteItemDTO> siteItems) {
		this.siteItems = siteItems;
	}

	public Long getRentalSiteId() {
		return rentalSiteId;
	}

	public void setRentalSiteId(Long rentalSiteId) {
		this.rentalSiteId = rentalSiteId;
	}

	public List<RentalSiteRulesDTO> getSiteRules() {
		return siteRules;
	}

	public void setSiteRules(List<RentalSiteRulesDTO> siteRules) {
		this.siteRules = siteRules;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}


	public String getOwnerType() {
		return ownerType;
	}


	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}


	public Long getCommunityId() {
		return ownerId;
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


	public Long getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}


	public java.lang.Byte getStatus() {
		return status;
	}


	public void setStatus(java.lang.Byte status) {
		this.status = status;
	}
 
}
