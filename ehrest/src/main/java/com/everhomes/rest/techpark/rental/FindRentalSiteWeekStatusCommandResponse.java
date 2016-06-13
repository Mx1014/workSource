package com.everhomes.rest.techpark.rental;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class FindRentalSiteWeekStatusCommandResponse {
	private Long rentalSiteId;
	private String ownerType;
	private Long ownerId;
	private String ownerName;
	private String siteType;
	private String contactNum;
	private String siteName; 
	private String introduction;
	private String notice; 
	private Long anchorTime;
	@ItemType(RentalSiteDayRulesDTO.class)
	private List<RentalSiteDayRulesDTO> siteDays;
	
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

	 
	public Long getRentalSiteId() {
		return rentalSiteId;
	}

	public void setRentalSiteId(Long rentalSiteId) {
		this.rentalSiteId = rentalSiteId;
	}
 

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
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


	public List<RentalSiteDayRulesDTO> getSiteDays() {
		return siteDays;
	}


	public void setSiteDays(List<RentalSiteDayRulesDTO> siteDays) {
		this.siteDays = siteDays;
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


	public String getOwnerName() {
		return ownerName;
	}


	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}


	public String getContactNum() {
		return contactNum;
	}


	public void setContactNum(String contactNum) {
		this.contactNum = contactNum;
	}


	public Long getAnchorTime() {
		return anchorTime;
	}


	public void setAnchorTime(Long anchorTime) {
		this.anchorTime = anchorTime;
	}
 

 
}
