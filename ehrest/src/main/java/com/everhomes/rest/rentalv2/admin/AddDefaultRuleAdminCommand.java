package com.everhomes.rest.rentalv2.admin;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.rentalv2.RentalV2ResourceType;
import com.everhomes.rest.rentalv2.RuleSourceType;
import com.everhomes.rest.rentalv2.SiteItemDTO;
import com.everhomes.rest.rentalv2.SiteStructureDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: ownerType</li>
 * <li>ownerId: 园区id</li>
 * <li>resourceType: resourceType {@link RentalV2ResourceType}</li>
 * <li>sourceType: sourceType 默认规则：default_rule， 资源规则：resource_rule{@link RuleSourceType}</li>
 * <li>sourceId: sourceId</li>
 * <li>resourceTypeId: 图标id</li>
 * <li>autoAssign: 是否需要自动分配资源</li>
 * <li>multiUnit: 是否允许预约多个场所</li>
 * <li>needPay: 是否需要支付</li>
 * <li>multiTimeInterval: 是否允许预约多个时段</li>
 * <li>attachments: 预约需要提交的信息 {@link com.everhomes.rest.rentalv2.admin.AttachmentConfigDTO}</li>
 * <li>rentalEndTime: 至少提前预约时间</li>
 * <li>rentalStartTime: 最多提前预约时间</li>
 * <li>timeIntervals: 开放时段 {@link com.everhomes.rest.rentalv2.admin.TimeIntervalDTO}</li>
 * <li>beginDate: 开放日期始</li>
 * <li>endDate: 开放日期终</li>
 * <li>openWeekday: 开放日期，从周日到周六是0123456，开放哪天就在数组传哪天</li>
 * <li>closeDates: 关闭日期</li>
 * <li>siteCounts: 可预约个数</li>
 * <li>siteNumbers: 用户填写的可预约个数个场所编号 {@link com.everhomes.rest.rentalv2.admin.SiteNumberDTO}</li>
 * <li>refundFlag: 是否允许退款</li>
 * <li>refundRatio: 退款比例</li>
 * <li>rentalStartTimeFlag: 最多提前预约时间标志 1：限制 0：不限制 {@link com.everhomes.rest.rentalv2.NormalFlag}</li>
 * <li>rentalEndTimeFlag: 至少提前预约时间标志 1：限制 0：不限制 {@link com.everhomes.rest.rentalv2.NormalFlag}</li>
 * <li>rentalTypes: rentalTypes</li>
 * <li>structures: 基础设施</li>
 * <li>priceRules: priceRules {@link com.everhomes.rest.rentalv2.admin.PriceRuleDTO}</li>
 * <li>pricePackages: pricePackages {@link com.everhomes.rest.rentalv2.admin.PricePackageDTO}</li>
 * <li>halfDayTimeIntervals: 半天时间设置 {@link com.everhomes.rest.rentalv2.admin.TimeIntervalDTO}</li>
 * <li>refundStrategies: 退款规则{@link com.everhomes.rest.rentalv2.admin.RentalOrderRuleDTO}</li>
 * <li>overtimeStrategies: 超时规则{@link com.everhomes.rest.rentalv2.admin.RentalOrderRuleDTO}</li>
 * <li>siteItems: 付费物资</li>
 * <li>remarkFlag: 备注字段是否必填 0否 1是</li>
 * <li>remark: 备注显示文案</li>
 * </ul>
 */
public class AddDefaultRuleAdminCommand {
	@NotNull
	private String ownerType;
	@NotNull
	private Long ownerId;

	private String resourceType;

	private String sourceType;
	private Long sourceId;

	@NotNull
	private Long resourceTypeId;


	private Byte needPay;
	private Byte multiTimeInterval;
	@ItemType(AttachmentConfigDTO.class)
	private List<AttachmentConfigDTO> attachments;
	private Long rentalEndTime;
	private Long rentalStartTime;
	@ItemType(TimeIntervalDTO.class)
	private List<TimeIntervalDTO> timeIntervals;
	private Long beginDate;
	private Long endDate;

	@ItemType(Integer.class)
	private List<Integer> openWeekday;
	@ItemType(Long.class)
	private List<Long> closeDates;

	private Byte refundFlag;
	private Integer refundRatio;

	private Byte rentalStartTimeFlag;
	private Byte rentalEndTimeFlag;

	private Double dayOpenTime;
	private Double dayCloseTime;
	private Byte holidayOpenFlag;
	private Byte holidayType;

	@ItemType(Byte.class)
	private List<Byte> rentalTypes;
	@ItemType(PriceRuleDTO.class)
	private List<PriceRuleDTO> priceRules;

	@ItemType(PricePackageDTO.class)
	private List<PricePackageDTO> pricePackages;

	@ItemType(TimeIntervalDTO.class)
	private List<TimeIntervalDTO> halfDayTimeIntervals;

	@ItemType(RentalOpenTimeDTO.class)
	private List<RentalOpenTimeDTO> openTimes;

	private List<SiteStructureDTO> structures;


	private Byte refundStrategy;
	private Byte overtimeStrategy;

	@ItemType(RentalOrderRuleDTO.class)
	private List<RentalOrderRuleDTO> refundStrategies;
	@ItemType(RentalOrderRuleDTO.class)
	private List<RentalOrderRuleDTO> overtimeStrategies;
	@ItemType(SiteItemDTO.class)
	private List<SiteItemDTO> siteItems;
	private List<RentalRefundTipDTO> refundTips;

	private Byte remarkFlag;
	private String remark;
	private Byte fileFlag;
	private String previewUsingImageUri;
	private String previewIdleImageUri;
	private String shopName;
	private String shopNo;
	private String shopUrl;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Byte getRefundStrategy() {
		return refundStrategy;
	}

	public void setRefundStrategy(Byte refundStrategy) {
		this.refundStrategy = refundStrategy;
	}

	public Byte getOvertimeStrategy() {
		return overtimeStrategy;
	}

	public void setOvertimeStrategy(Byte overtimeStrategy) {
		this.overtimeStrategy = overtimeStrategy;
	}

	public List<RentalOrderRuleDTO> getRefundStrategies() {
		return refundStrategies;
	}

	public void setRefundStrategies(List<RentalOrderRuleDTO> refundStrategies) {
		this.refundStrategies = refundStrategies;
	}

	public List<RentalOrderRuleDTO> getOvertimeStrategies() {
		return overtimeStrategies;
	}

	public void setOvertimeStrategies(List<RentalOrderRuleDTO> overtimeStrategies) {
		this.overtimeStrategies = overtimeStrategies;
	}

	public Double getDayOpenTime() {
		return dayOpenTime;
	}

	public void setDayOpenTime(Double dayOpenTime) {
		this.dayOpenTime = dayOpenTime;
	}

	public Double getDayCloseTime() {
		return dayCloseTime;
	}

	public void setDayCloseTime(Double dayCloseTime) {
		this.dayCloseTime = dayCloseTime;
	}

	public List<PricePackageDTO> getPricePackages() {
		return pricePackages;
	}

	public void setPricePackages(List<PricePackageDTO> pricePackages) {
		this.pricePackages = pricePackages;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public List<Byte> getRentalTypes() {
		return rentalTypes;
	}

	public void setRentalTypes(List<Byte> rentalTypes) {
		this.rentalTypes = rentalTypes;
	}

	public List<PriceRuleDTO> getPriceRules() {
		return priceRules;
	}

	public void setPriceRules(List<PriceRuleDTO> priceRules) {
		this.priceRules = priceRules;
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

	public Long getResourceTypeId() {
		return resourceTypeId;
	}

	public void setResourceTypeId(Long resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
	}

	public Byte getNeedPay() {
		return needPay;
	}

	public void setNeedPay(Byte needPay) {
		this.needPay = needPay;
	}

	public Byte getMultiTimeInterval() {
		return multiTimeInterval;
	}

	public void setMultiTimeInterval(Byte multiTimeInterval) {
		this.multiTimeInterval = multiTimeInterval;
	}

	public List<AttachmentConfigDTO> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<AttachmentConfigDTO> attachments) {
		this.attachments = attachments;
	}

	public Long getRentalEndTime() {
		return rentalEndTime;
	}

	public void setRentalEndTime(Long rentalEndTime) {
		this.rentalEndTime = rentalEndTime;
	}

	public Long getRentalStartTime() {
		return rentalStartTime;
	}

	public void setRentalStartTime(Long rentalStartTime) {
		this.rentalStartTime = rentalStartTime;
	}

	public List<TimeIntervalDTO> getTimeIntervals() {
		return timeIntervals;
	}

	public void setTimeIntervals(List<TimeIntervalDTO> timeIntervals) {
		this.timeIntervals = timeIntervals;
	}

	public Long getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Long beginDate) {
		this.beginDate = beginDate;
	}

	public Long getEndDate() {
		return endDate;
	}

	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}

	public List<Integer> getOpenWeekday() {
		return openWeekday;
	}

	public void setOpenWeekday(List<Integer> openWeekday) {
		this.openWeekday = openWeekday;
	}

	public List<Long> getCloseDates() {
		return closeDates;
	}

	public void setCloseDates(List<Long> closeDates) {
		this.closeDates = closeDates;
	}

	public Byte getRefundFlag() {
		return refundFlag;
	}

	public void setRefundFlag(Byte refundFlag) {
		this.refundFlag = refundFlag;
	}

	public Integer getRefundRatio() {
		return refundRatio;
	}

	public void setRefundRatio(Integer refundRatio) {
		this.refundRatio = refundRatio;
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

	public List<TimeIntervalDTO> getHalfDayTimeIntervals() {
		return halfDayTimeIntervals;
	}

	public void setHalfDayTimeIntervals(List<TimeIntervalDTO> halfDayTimeIntervals) {
		this.halfDayTimeIntervals = halfDayTimeIntervals;
	}

	public List<SiteItemDTO> getSiteItems() {
		return siteItems;
	}

	public void setSiteItems(List<SiteItemDTO> siteItems) {
		this.siteItems = siteItems;
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

	public List<RentalOpenTimeDTO> getOpenTimes() {
		return openTimes;
	}

	public void setOpenTimes(List<RentalOpenTimeDTO> openTimes) {
		this.openTimes = openTimes;
	}

	public Byte getRemarkFlag() {
		return remarkFlag;
	}

	public void setRemarkFlag(Byte remarkFlag) {
		this.remarkFlag = remarkFlag;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<RentalRefundTipDTO> getRefundTips() {
		return refundTips;
	}

	public void setRefundTips(List<RentalRefundTipDTO> refundTips) {
		this.refundTips = refundTips;
	}

	public List<SiteStructureDTO> getStructures() {
		return structures;
	}

	public void setStructures(List<SiteStructureDTO> structures) {
		this.structures = structures;
	}

	public Byte getFileFlag() {
		return fileFlag;
	}

	public void setFileFlag(Byte fileFlag) {
		this.fileFlag = fileFlag;
	}

	public String getPreviewUsingImageUri() {
		return previewUsingImageUri;
	}

	public void setPreviewUsingImageUri(String previewUsingImageUri) {
		this.previewUsingImageUri = previewUsingImageUri;
	}

	public String getPreviewIdleImageUri() {
		return previewIdleImageUri;
	}

	public void setPreviewIdleImageUri(String previewIdleImageUri) {
		this.previewIdleImageUri = previewIdleImageUri;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopNo() {
		return shopNo;
	}

	public void setShopNo(String shopNo) {
		this.shopNo = shopNo;
	}

	public String getShopUrl() {
		return shopUrl;
	}

	public void setShopUrl(String shopUrl) {
		this.shopUrl = shopUrl;
	}
}
