package com.everhomes.rest.techpark.rental;

import java.math.BigDecimal;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.techpark.rental.admin.SiteOwnerDTO;
import com.everhomes.util.StringHelper;
/**
 * <ul>
 * 场所
 * <li>rentalSiteId：场所id</li> 
 * <li>siteName：场所名称</li>
 * <li>buildingName：楼栋名称</li>
 * <li>address：位置</li>
 * <li>avgPrice：平均价格</li>
 * <li>spec：规格：用户设置座位数等</li>
 * <li>companyId：场所隶属的公司id</li>
 * <li>ownId：负责人id</li>
 * <li>contactPhonenum：电话号码</li>
 * <li>siteItems：场所商品</li>
 * <li>	coverUri：	封面图uri	</li>
 * <li>	discountType：     	折扣信息：0不打折 1满减优惠2比例折扣	</li>
 * <li>	fullPrice：        	满XX元	</li>
 * <li>	cutPrice：         	减XX元	</li>
 * <li>	discountRatio：    	折扣比例	</li>
 * <li>	rentalType：0: 按小时预定  1-半天 2-天 3-支持晚上的半天	</li>
 * <li>	rentalStep：步长，每次最少预定多少个单元格（目前默认都是1）</li>
 * <li>	exclusiveFlag：    	是否为独占资源0否 1 是	</li>
 * <li>	autoAssign：       	是否动态分配 1是 0否	</li>
 * <li>	multiUnit：        	是否允许预约多个场所 1是 0否	</li>
 * <li>	multiTimeInterval：	是否允许预约多个时段 1是 0否	</li>
 * <li>	cancelFlag：       	是否允许取消 1是 0否	</li>
 * <li>	needPay：          	是否需要支付 1是 0否	</li>
 * </ul>
 */
public class RentalSiteDTO {
	private Long rentalSiteId; 
	private String siteName;
	private String buildingName;
	private String address;
	private BigDecimal avgPrice;
	private String spec;
	private String companyName;
	private String contactName;
	private String contactPhonenum;
	private String introduction;
	private String notice; 
	private java.lang.String     coverUri;
	private java.lang.String     coverUrl;
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
	private java.lang.Byte     status;
	private Long createTime;
	@ItemType(SiteItemDTO.class)
	private List<SiteItemDTO> siteItems;
//	@ItemType(RentalSiteRulesDTO.class)
//	private List<RentalSiteRulesDTO> siteRules;
	@ItemType(RentalSitePicDTO.class)
	private List<RentalSitePicDTO> sitePics;
	@ItemType(SiteOwnerDTO.class)
	private List<SiteOwnerDTO> owners;
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
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

//	public List<RentalSiteRulesDTO> getSiteRules() {
//		return siteRules;
//	}
//
//	public void setSiteRules(List<RentalSiteRulesDTO> siteRules) {
//		this.siteRules = siteRules;
//	}

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


	public BigDecimal getAvgPrice() {
		return avgPrice;
	}


	public void setAvgPrice(BigDecimal avgPrice) {
		this.avgPrice = avgPrice;
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


	public java.lang.String getCoverUrl() {
		return coverUrl;
	}


	public void setCoverUrl(java.lang.String coverUrl) {
		this.coverUrl = coverUrl;
	}


	public java.lang.String getCoverUri() {
		return coverUri;
	}


	public void setCoverUri(java.lang.String coverUri) {
		this.coverUri = coverUri;
	}


	public List<RentalSitePicDTO> getSitePics() {
		return sitePics;
	}


	public void setSitePics(List<RentalSitePicDTO> sitePics) {
		this.sitePics = sitePics;
	}
 
}
