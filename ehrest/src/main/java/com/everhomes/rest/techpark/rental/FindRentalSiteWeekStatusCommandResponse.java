package com.everhomes.rest.techpark.rental;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class FindRentalSiteWeekStatusCommandResponse {
	private Long rentalSiteId; 
	private String contactNum;
	private String siteName; 
	private String introduction;
	private String notice;  
	private java.lang.Byte       discountType;
	private java.math.BigDecimal fullPrice;
	private java.math.BigDecimal cutPrice;
	private java.lang.Double     discountRatio;
	private java.lang.Byte       rentalType;
	private java.lang.Integer    rentalStep;
	private java.lang.Byte       exclusiveFlag;
	private java.lang.Byte       autoAssign;
	private java.lang.Byte       multiUnit;
	private java.lang.Byte       multiTimeInterval;
	private java.lang.Byte       cancelFlag;
	private java.lang.Byte       needPay;
	private Long anchorTime;
	@ItemType(RentalSiteDayRulesDTO.class)
	private List<RentalSiteDayRulesDTO> siteDays;
	@ItemType(RentalSitePicDTO.class)
	private List<RentalSitePicDTO> sitePics;
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
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


	public java.lang.Byte getDiscountType() {
		return discountType;
	}


	public void setDiscountType(java.lang.Byte discountType) {
		this.discountType = discountType;
	}


	public java.math.BigDecimal getFullPrice() {
		return fullPrice;
	}


	public void setFullPrice(java.math.BigDecimal fullPrice) {
		this.fullPrice = fullPrice;
	}


	public java.math.BigDecimal getCutPrice() {
		return cutPrice;
	}


	public void setCutPrice(java.math.BigDecimal cutPrice) {
		this.cutPrice = cutPrice;
	}


	public java.lang.Double getDiscountRatio() {
		return discountRatio;
	}


	public void setDiscountRatio(java.lang.Double discountRatio) {
		this.discountRatio = discountRatio;
	}


	public java.lang.Byte getRentalType() {
		return rentalType;
	}


	public void setRentalType(java.lang.Byte rentalType) {
		this.rentalType = rentalType;
	}


	public java.lang.Integer getRentalStep() {
		return rentalStep;
	}


	public void setRentalStep(java.lang.Integer rentalStep) {
		this.rentalStep = rentalStep;
	}


	public java.lang.Byte getExclusiveFlag() {
		return exclusiveFlag;
	}


	public void setExclusiveFlag(java.lang.Byte exclusiveFlag) {
		this.exclusiveFlag = exclusiveFlag;
	}


	public java.lang.Byte getAutoAssign() {
		return autoAssign;
	}


	public void setAutoAssign(java.lang.Byte autoAssign) {
		this.autoAssign = autoAssign;
	}


	public java.lang.Byte getMultiUnit() {
		return multiUnit;
	}


	public void setMultiUnit(java.lang.Byte multiUnit) {
		this.multiUnit = multiUnit;
	}


	public java.lang.Byte getMultiTimeInterval() {
		return multiTimeInterval;
	}


	public void setMultiTimeInterval(java.lang.Byte multiTimeInterval) {
		this.multiTimeInterval = multiTimeInterval;
	}


	public java.lang.Byte getCancelFlag() {
		return cancelFlag;
	}


	public void setCancelFlag(java.lang.Byte cancelFlag) {
		this.cancelFlag = cancelFlag;
	}


	public java.lang.Byte getNeedPay() {
		return needPay;
	}


	public void setNeedPay(java.lang.Byte needPay) {
		this.needPay = needPay;
	}


	public List<RentalSitePicDTO> getSitePics() {
		return sitePics;
	}


	public void setSitePics(List<RentalSitePicDTO> sitePics) {
		this.sitePics = sitePics;
	}
 

 
}
