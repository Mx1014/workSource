package com.everhomes.rest.rentalv2.admin;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.rentalv2.RentalV2ResourceType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>resourceType: resourceType {@link RentalV2ResourceType}</li>
 * <li>rentalSiteId: 资源id</li>
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
 * <li>dayOpenTime: 每天的开放时间</li>
 * <li>dayCloseTime: 每天的关闭时间</li>
 * <li>openWeekday: 开放日期，从周日到周六是0123456，开放哪天就在数组传哪天Integer列表</li>
 * <li>closeDates: 关闭日期</li>
 * <li>siteCounts: 可预约个数</li>
 * <li>siteNumbers: 用户填写的可预约个数个场所编号{@link com.everhomes.rest.rentalv2.admin.SiteNumberDTO}}</li>
 * <li>refundFlag: 是否允许退款</li>
 * <li>refundRatio: 退款比例</li>
 * <li>rentalStartTimeFlag: 最多提前预约时间标志 1：限制 0：不限制 {@link com.everhomes.rest.rentalv2.NormalFlag}</li>
 * <li>rentalEndTimeFlag: 至少提前预约时间标志 1：限制 0：不限制 {@link com.everhomes.rest.rentalv2.NormalFlag}</li>
 * <li>rentalTypes: 时间单元类型列表 {@link com.everhomes.rest.rentalv2.RentalType}</li>
 * <li>priceRules: 价格策略列表 {@link com.everhomes.rest.rentalv2.admin.PriceRuleDTO}</li>
 * <li>pricePackages: 套餐价格表{@link com.everhomes.rest.rentalv2.admin.PricePackageDTO}</li>
 * <li>halfDayTimeIntervals: 半天时间设置 {@link com.everhomes.rest.rentalv2.admin.TimeIntervalDTO}</li>
 * </ul>
 */
public class AddRentalSiteRulesAdminCommand {
	@NotNull
	private Long rentalSiteId;
	private String resourceType;
	//	private Byte exclusiveFlag;
//	private Double unit;
	private Byte autoAssign;
	private Byte multiUnit;
	private Byte needPay;
	private Byte multiTimeInterval;
	@ItemType(AttachmentConfigDTO.class)
	private List<AttachmentConfigDTO> attachments;
	private Long rentalEndTime;
	private Long rentalStartTime;
	//	private Double timeStep;
	@ItemType(TimeIntervalDTO.class)
	private List<TimeIntervalDTO> timeIntervals;
	private Long beginDate;
	private Long endDate;
	private Double dayOpenTime;
	private Double dayCloseTime;

	@ItemType(Integer.class)
	private List<Integer> openWeekday;
	@ItemType(Long.class)
	private List<Long> closeDates;
	private Double siteCounts;
	@ItemType(SiteNumberDTO.class)
	private List<SiteNumberDTO> siteNumbers;
	//	private Long cancelTime;
	private Byte refundFlag;
	private Integer refundRatio;

	private Byte rentalStartTimeFlag;
	private Byte rentalEndTimeFlag;

	@ItemType(Byte.class)
	private List<Byte> rentalTypes;
	@ItemType(PriceRuleDTO.class)
	private List<PriceRuleDTO> priceRules;
	@ItemType(PricePackageDTO.class)
	private List<PricePackageDTO> pricePackages;
//	@Deprecated
//	private Byte rentalType;
//	@Deprecated
//	private BigDecimal workdayPrice;
//	@Deprecated
//	private BigDecimal weekendPrice;
//	@Deprecated
//	private BigDecimal orgMemberWorkdayPrice;
//	@Deprecated
//	private BigDecimal orgMemberWeekendPrice;
//	@Deprecated
//	private BigDecimal approvingUserWorkdayPrice;
//	@Deprecated
//	private BigDecimal approvingUserWeekendPrice;
//	@Deprecated
//	private Byte discountType;
//	@Deprecated
//	private java.math.BigDecimal fullPrice;
//	@Deprecated
//	private java.math.BigDecimal cutPrice;
//	@Deprecated
//    private Double discountRatio;

	@ItemType(TimeIntervalDTO.class)
	private List<TimeIntervalDTO> halfDayTimeIntervals;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
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

	public Long getRentalSiteId() {
		return rentalSiteId;
	}

	public void setRentalSiteId(Long rentalSiteId) {
		this.rentalSiteId = rentalSiteId;
	}

	public Byte getAutoAssign() {
		return autoAssign;
	}

	public void setAutoAssign(Byte autoAssign) {
		this.autoAssign = autoAssign;
	}

	public Byte getMultiUnit() {
		return multiUnit;
	}

	public void setMultiUnit(Byte multiUnit) {
		this.multiUnit = multiUnit;
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

	public Double getSiteCounts() {
		return siteCounts;
	}

	public void setSiteCounts(Double siteCounts) {
		this.siteCounts = siteCounts;
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

	public List<PricePackageDTO> getPricePackages() {
		return pricePackages;
	}

	public void setPricePackages(List<PricePackageDTO> pricePackages) {
		this.pricePackages = pricePackages;
	}

	public List<SiteNumberDTO> getSiteNumbers() {
		return siteNumbers;
	}

	public void setSiteNumbers(List<SiteNumberDTO> siteNumbers) {
		this.siteNumbers = siteNumbers;
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
}
