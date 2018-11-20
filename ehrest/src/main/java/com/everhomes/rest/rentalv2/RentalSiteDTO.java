package com.everhomes.rest.rentalv2;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.rentalv2.admin.AttachmentConfigDTO;
import com.everhomes.rest.rentalv2.admin.SiteNumberDTO;
import com.everhomes.rest.rentalv2.admin.SiteOwnerDTO;
import com.everhomes.util.StringHelper;
/**
 * <ul>
 * 场所
 * <li>rentalSiteId：场所id</li> 
 * <li>siteName：场所名称</li>
 * <li>buildingName：楼栋名称</li>
 * <li>address：位置</li>
 * <li>avgPrice：平均价格</li>
 * <li>avgPriceStr：平均价格字段(新需求新增加新版本用)</li>
 * <li>spec：规格：用户设置座位数等</li>
 * <li>companyId：场所隶属的公司id</li>
 * <li>longitude：经度</li>
 * <li>latitude：纬度</li>
 * <li>chargeUid：负责人id</li>
 * <li>chargeName：负责人名称</li> 
 * <li>communityId：所属园区id</li> 
 * <li>communityName：所属园区名称</li> 
 * <li>contactPhonenum：电话号码</li>
 * <li>introduction：详情</li>
 * <li>notice ：提示文字</li>
 * <li>	coverUri：	封面图uri	</li>
 * <li>	coverUrl：	封面图url	</li>
 * <li>	discountType：     	折扣信息：0不打折 1满减优惠2比例折扣	</li>
 * <li>	fullPrice：        	满XX元	</li>
 * <li>	cutPrice：         	减XX元	</li>
 * <li>	discountRatio：    	折扣比例	</li>
 * <li>	rentalType：0: 按小时预定  1-半天 2-天 3-支持晚上的半天	</li>
 * <li>	timeStep：按小时预定步长，每个单元格代表多少小时，浮点型</li>
 * <li>	dayBeginTime：按小时预定，每天最早时间</li>
 * <li>	dayEndTime：按小时预定，每天最晚时间</li>
 * <li>	exclusiveFlag：    	是否为独占资源0否 1 是	</li>
 * <li>	autoAssign：       	是否动态分配 1是 0否	</li>
 * <li>	multiUnit：        	是否允许预约多个场所 1是 0否	</li>
 * <li>	multiTimeInterval：	是否允许预约多个时段 1是 0否	</li>
 * <li>	cancelFlag：       	是否允许取消 1是 0否	</li>
 * <li>	needPay：          	是否需要支付 1是 0否	</li>
 * <li>	status：资源状态</li>
 * <li>	createTime：创建时间</li>
 * <li>	siteCounts：资源数量</li>
 * <li>	siteNumbers：资源编号列表 {String}</li>
 * <li>	siteItems：资源物品列表 {@link com.everhomes.rest.rentalv2.SiteItemDTO}</li>
 * <li>	sitePics： List资源图片列表 {@link com.everhomes.rest.rentalv2.RentalSitePicDTO}</li>
 * <li>	sitefiles： List资源文件列表 {@link com.everhomes.rest.rentalv2.RentalSiteFileDTO}</li>
 * <li>	owners： List资源可显示的园区范围列表 {@link com.everhomes.rest.rentalv2.admin.SiteOwnerDTO}</li>
 * <li>attachments: 可添加的附件{@link com.everhomes.rest.rentalv2.admin.AttachmentConfigDTO}</li>
 * <li>confirmationPrompt: 确认提示(非必填)</li>
 * <li>offlineCashierAddress: 线下支付收银地址</li>
 * <li>offlinePayeeUid: 线下支付收款人id</li>
 * <li>offlinePayeeName: 线下支付收款人姓名</li>
 * <li>detailUrl: 详情Url</li>
 * <li>rentalEndTime: 至少提前预约时间</li>
 * <li>rentalStartTime: 最多提前预约时间</li>
 * <li>rentalEndTimeFlag: 至少提前预约时间标志 1：限制 0：不限制 {@link com.everhomes.rest.rentalv2.NormalFlag}</li>
 * <li>rentalStartTimeFlag: 最多提前预约时间标志 1：限制 0：不限制 {@link com.everhomes.rest.rentalv2.NormalFlag}</li>
 * <li>resourceTypeId: 资源类型id</li>
 * <li>payMode: 支付模式</li>
 * <li>identify: 资源类型的类型 {@link com.everhomes.rest.rentalv2.RentalV2ResourceType}</li>
 * <li>sitePriceRules: 价格策略，参考{@link com.everhomes.rest.rentalv2.SitePriceRuleDTO}</li>
 * <li>unauthVisible: 非认证用户是否可见，参考{@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 * <li>AclinkId: 门禁组id</li>
 * <li>AclinkName: 门禁组名</li>
 * <li>holidayOpenFlag: 节假日是否开放预约 0不开放 1 开放</li>
 * <li>holidayType: 节假日类型 {@link com.everhomes.rest.rentalv2.admin.RentalHolidayType}</li>
 * </ul>
 */
public class RentalSiteDTO {
	private Long rentalSiteId; 
	private String siteName;
	private String buildingName;
	private String address; 
	private Double longitude; 
	private Double latitude;
	private String chargeUid;
	private String chargeName; 
	private Long communityId;
	private String communityName;
	private BigDecimal avgPrice;
	private String avgPriceStr;
	private String spec;
	private String companyName;
	private String contactName;
	private String contactPhonenum;
	private String introduction;
	private String notice; 
	private java.lang.String     coverUri;
	private java.lang.String     coverUrl;
	private Double   timeStep;
	private Long  dayBeginTime;
	private Long  dayEndTime;
	private java.lang.Byte       exclusiveFlag;
	private java.lang.Byte       autoAssign;
	private java.lang.Byte       multiUnit;
	private java.lang.Byte       multiTimeInterval;
	private java.lang.Byte       cancelFlag;
	private java.lang.Byte       needPay;
	private java.lang.Byte     status;
	private Long createTime;
	private Double siteCounts;
	@ItemType(SiteNumberDTO.class)
	private List<SiteNumberDTO> siteNumbers;
	@ItemType(SiteItemDTO.class)
	private List<SiteItemDTO> siteItems;
//	@ItemType(RentalSiteRulesDTO.class)
//	private List<RentalSiteRulesDTO> siteRules;
	@ItemType(RentalSitePicDTO.class)
	private List<RentalSitePicDTO> sitePics;
	private List<RentalSiteFileDTO> siteFiles;
	@ItemType(SiteOwnerDTO.class)
	private List<SiteOwnerDTO> owners;
	@ItemType(AttachmentConfigDTO.class)
	private List<AttachmentConfigDTO> attachments;
	private String confirmationPrompt;
    private String offlineCashierAddress;
    private Long offlinePayeeUid;
    private String offlinePayeeName;

	private Byte rentalStartTimeFlag;
	private Byte rentalEndTimeFlag;
	private Long rentalStartTime;
	private Long rentalEndTime;
    private String detailUrl;
    private String reserveRouteUrl;

    private Long resourceTypeId;
    private Byte payMode;
    private String identify;

    private Byte unauthVisible;

	private String aclinkId;
	private String aclinkName;
    @ItemType(SitePriceRuleDTO.class)
    private List<SitePriceRuleDTO> sitePriceRules;
	private Byte holidayOpenFlag;
	private Byte holidayType;
	private String refundTip;
    @Deprecated
	private java.lang.Byte       rentalType;
    
	public Byte getUnauthVisible() {
		return unauthVisible;
	}

	public void setUnauthVisible(Byte unauthVisible) {
		this.unauthVisible = unauthVisible;
	}

	public List<SitePriceRuleDTO> getSitePriceRules() {
		return sitePriceRules;
	}

	public void setSitePriceRules(List<SitePriceRuleDTO> sitePriceRules) {
		this.sitePriceRules = sitePriceRules;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Long getResourceTypeId() {
		return resourceTypeId;
	}

	public void setResourceTypeId(Long resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
	}

	public Byte getPayMode() {
		return payMode;
	}

	public void setPayMode(Byte payMode) {
		this.payMode = payMode;
	}

	public String getReserveRouteUrl() {
		return reserveRouteUrl;
	}

	public void setReserveRouteUrl(String reserveRouteUrl) {
		this.reserveRouteUrl = reserveRouteUrl;
	}

	public String getDetailUrl() {
		return detailUrl;
	}

	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
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

	public Double getLongitude() {
		return longitude;
	}


	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}


	public Double getLatitude() {
		return latitude;
	}


	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}


	public String getChargeUid() {
		return chargeUid;
	}

	public void setChargeUid(String chargeUid) {
		this.chargeUid = chargeUid;
	}

	public String getChargeName() {
		return chargeName;
	}


	public void setChargeName(String chargeName) {
		this.chargeName = chargeName;
	}


	public Long getCommunityId() {
		return communityId;
	}


	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}


	public String getCommunityName() {
		return communityName;
	}


	public void setCommunityName(String communityName) {
		this.communityName = communityName;
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

	public Byte getHolidayOpenFlag() {
		return holidayOpenFlag;
	}

	public void setHolidayOpenFlag(Byte holidayOpenFlag) {
		this.holidayOpenFlag = holidayOpenFlag;
	}

	public Byte getHolidayType() {
		return holidayType;
	}

	public void setHolidayType(Byte holidayType) {
		this.holidayType = holidayType;
	}

	public java.lang.Byte getRentalType() {
		return rentalType;
	}


	public void setRentalType(java.lang.Byte rentalType) {
		this.rentalType = rentalType;
	}


	  

	public Double getTimeStep() {
		return timeStep;
	}


	public void setTimeStep(Double timeStep) {
		this.timeStep = timeStep;
	}

 

	public Long getDayBeginTime() {
		return dayBeginTime;
	}


	public void setDayBeginTime(Long dayBeginTime) {
		this.dayBeginTime = dayBeginTime;
	}


	public Long getDayEndTime() {
		return dayEndTime;
	}


	public void setDayEndTime(Long dayEndTime) {
		this.dayEndTime = dayEndTime;
	}


	public List<SiteOwnerDTO> getOwners() {
		return owners;
	}


	public void setOwners(List<SiteOwnerDTO> owners) {
		this.owners = owners;
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


	public List<AttachmentConfigDTO> getAttachments() {
		return attachments;
	}


	public void setAttachments(List<AttachmentConfigDTO> attachments) {
		this.attachments = attachments;
	}


	public Double getSiteCounts() {
		return siteCounts;
	}


	public void setSiteCounts(Double siteCounts) {
		this.siteCounts = siteCounts;
	}


	public List<SiteNumberDTO> getSiteNumbers() {
		return siteNumbers;
	}

	public void setSiteNumbers(List<SiteNumberDTO> siteNumbers) {
		this.siteNumbers = siteNumbers;
	}

	public String getAvgPriceStr() {
		return avgPriceStr;
	}


	public void setAvgPriceStr(String avgPriceStr) {
		this.avgPriceStr = avgPriceStr;
	}


	public String getConfirmationPrompt() {
		return confirmationPrompt;
	}


	public void setConfirmationPrompt(String confirmationPrompt) {
		this.confirmationPrompt = confirmationPrompt;
	}


	public String getOfflineCashierAddress() {
		return offlineCashierAddress;
	}


	public void setOfflineCashierAddress(String offlineCashierAddress) {
		this.offlineCashierAddress = offlineCashierAddress;
	}


	public Long getOfflinePayeeUid() {
		return offlinePayeeUid;
	}


	public void setOfflinePayeeUid(Long offlinePayeeUid) {
		this.offlinePayeeUid = offlinePayeeUid;
	}


	public String getOfflinePayeeName() {
		return offlinePayeeName;
	}


	public void setOfflinePayeeName(String offlinePayeeName) {
		this.offlinePayeeName = offlinePayeeName;
	}

	public Byte getRentalStartTimeFlag() {
		return rentalStartTimeFlag;
	}

	public void setRentalStartTimeFlag(Byte rentalStartTimeFlag) {
		this.rentalStartTimeFlag = rentalStartTimeFlag;
	}

	public Byte getRentalEndTimeFlag() {
		return rentalEndTimeFlag;
	}

	public void setRentalEndTimeFlag(Byte rentalEndTimeFlag) {
		this.rentalEndTimeFlag = rentalEndTimeFlag;
	}

	public Long getRentalStartTime() {
		return rentalStartTime;
	}

	public void setRentalStartTime(Long rentalStartTime) {
		this.rentalStartTime = rentalStartTime;
	}

	public Long getRentalEndTime() {
		return rentalEndTime;
	}

	public void setRentalEndTime(Long rentalEndTime) {
		this.rentalEndTime = rentalEndTime;
	}

	public String getAclinkId() {
		return aclinkId;
	}

	public void setAclinkId(String aclinkId) {
		this.aclinkId = aclinkId;
	}

	public String getAclinkName() {
		return aclinkName;
	}

	public void setAclinkName(String aclinkName) {
		this.aclinkName = aclinkName;
	}

	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}

	public String getRefundTip() {
		return refundTip;
	}

	public void setRefundTip(String refundTip) {
		this.refundTip = refundTip;
	}

	public List<RentalSiteFileDTO> getSiteFiles() {
		return siteFiles;
	}

	public void setSiteFiles(List<RentalSiteFileDTO> siteFiles) {
		this.siteFiles = siteFiles;
	}
}
