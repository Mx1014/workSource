package com.everhomes.rest.rentalv2;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.rentalv2.admin.AttachmentConfigDTO;
import com.everhomes.rest.rentalv2.admin.SiteNumberDTO;
import com.everhomes.rest.rentalv2.admin.TimeIntervalDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>beginTime: 开始时间(hour)</li>
 * <li>endTime: 结束时间(hour)</li>
 * <li>rentalSiteId: 资源id</li>
 * <li>autoAssign: 是否需要自动分配资源</li>
 * <li>multiUnit: 是否允许预约多个场所</li>
 * <li>needPay: 是否需要支付</li>
 * <li>multiTimeInterval: multiTimeInterval</li>
 * <li>rentalType: 预约类型，参考{@link com.everhomes.rest.rentalv2.RentalType}</li>
 * <li>priceType: 0 按时长定价 1 起步价模式</li>
 * <li>rentalEndTime: 至少提前预约时间</li>
 * <li>rentalStartTime: 最多提前预约时间</li>
 * <li>timeStep: timeStep</li>
 * <li>beginDate: 开放日期始</li>
 * <li>endDate: 开放日期终</li>
 * <li>openWeekday: 开放日期，从周日到周六是1234567，开放哪天就在数组传哪天List< Integer></li>
 * <li>closeDates: 关闭日期</li>
 * <li>workdayPrice: 工作日价格</li>
 * <li>weekendPrice: 周末价格</li>
 * <li>initiatePrice: 起步后价格</li>
 * <li>siteCounts: 可预约个数</li>
 * <li>siteNumbers: 用户填写的可预约个数个场所编号 {@link com.everhomes.rest.rentalv2.admin.SiteNumberDTO}</li>
 * <li>refundFlag: 是否允许退款</li>
 * <li>refundRatio: 退款比例</li>
 * <li>rentalStartTimeFlag: 最多提前预约时间标志 1：限制 0：不限制 {@link com.everhomes.rest.rentalv2.NormalFlag}</li>
 * <li>rentalEndTimeFlag: 至少提前预约时间标志 1：限制 0：不限制 {@link com.everhomes.rest.rentalv2.NormalFlag}</li>
 * <li>orgMemberWorkdayPrice: 企业内部工作日价格</li>
 * <li>orgMemberWeekendPrice: 企业内部节假日价格</li>
 * <li>orgMemberInitiatePrice: 集团内部起步后价格</li>
 * <li>approvingUserInitiatePrice: 外部客户起步后价格</li>
 * <li>approvingUserWorkdayPrice: 外部客户工作日价格</li>
 * <li>approvingUserWeekendPrice: 外部客户节假日价格</li>
 * <li>halfDayTimeIntervals: 半天时间设置 {@link com.everhomes.rest.rentalv2.admin.TimeIntervalDTO}</li>
 * </ul>
 */
public class AddRentalSiteSingleSimpleRule {

	private Double beginTime;
	private Double endTime;

	@NotNull
	private Long rentalSiteId;
	//	private Byte exclusiveFlag;
//	private Double unit;
	private Byte autoAssign;
	private Byte multiUnit;
	private Byte needPay;
	private Byte multiTimeInterval;
	//	@ItemType(AttachmentConfigDTO.class)
//	private List<AttachmentConfigDTO> attachments;
	private Byte rentalType;
	private Byte priceType;
	private Long rentalEndTime;
	private Long rentalStartTime;
	private Double timeStep;
	//	@ItemType(TimeIntervalDTO.class) 生成单元格时不需要 小时的参数，
//	private List<TimeIntervalDTO> timeIntervals;
	private Long beginDate;
	private Long endDate;

	@ItemType(Integer.class)
	private List<Integer> openWeekday;
	@ItemType(Long.class)
	private List<Long> closeDates;
	private BigDecimal workdayPrice;
	private BigDecimal originalPrice;
	private BigDecimal initiatePrice;
	private Double siteCounts;
	@ItemType(SiteNumberDTO.class)
	private List<SiteNumberDTO> siteNumbers;
	//	private Long cancelTime;
	private Byte refundFlag;
	private Integer refundRatio;

	private Byte rentalStartTimeFlag;
	private Byte rentalEndTimeFlag;
	private BigDecimal orgMemberWorkdayPrice;
	private BigDecimal orgMemberOriginalPrice;
	private BigDecimal orgMemberInitiatePrice;

	private BigDecimal approvingUserInitiatePrice;
	private BigDecimal approvingUserOriginalPrice;
	private BigDecimal approvingUserWorkdayPrice;
	private Byte userPriceType;

	@ItemType(TimeIntervalDTO.class)
	private List<TimeIntervalDTO> halfDayTimeIntervals;
	private String resourceType;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}


	public Double getBeginTime() {
		return beginTime;
	}


	public void setBeginTime(Double beginTime) {
		this.beginTime = beginTime;
	}


	public Double getEndTime() {
		return endTime;
	}


	public void setEndTime(Double endTime) {
		this.endTime = endTime;
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

	public Byte getRentalType() {
		return rentalType;
	}


	public void setRentalType(Byte rentalType) {
		this.rentalType = rentalType;
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


	public BigDecimal getWorkdayPrice() {
		return workdayPrice;
	}


	public void setWorkdayPrice(BigDecimal workdayPrice) {
		this.workdayPrice = workdayPrice;
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


	public Double getTimeStep() {
		return timeStep;
	}


	public void setTimeStep(Double timeStep) {
		this.timeStep = timeStep;
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

	public BigDecimal getOrgMemberWorkdayPrice() {
		return orgMemberWorkdayPrice;
	}

	public void setOrgMemberWorkdayPrice(BigDecimal orgMemberWorkdayPrice) {
		this.orgMemberWorkdayPrice = orgMemberWorkdayPrice;
	}

	public BigDecimal getApprovingUserWorkdayPrice() {
		return approvingUserWorkdayPrice;
	}

	public void setApprovingUserWorkdayPrice(BigDecimal approvingUserWorkdayPrice) {
		this.approvingUserWorkdayPrice = approvingUserWorkdayPrice;
	}

	public List<TimeIntervalDTO> getHalfDayTimeIntervals() {
		return halfDayTimeIntervals;
	}

	public Byte getPriceType() {
		return priceType;
	}

	public void setPriceType(Byte priceType) {
		this.priceType = priceType;
	}

	public BigDecimal getInitiatePrice() {
		return initiatePrice;
	}

	public void setInitiatePrice(BigDecimal initiatePrice) {
		this.initiatePrice = initiatePrice;
	}

	public BigDecimal getOrgMemberInitiatePrice() {
		return orgMemberInitiatePrice;
	}

	public void setOrgMemberInitiatePrice(BigDecimal orgMemberInitiatePrice) {
		this.orgMemberInitiatePrice = orgMemberInitiatePrice;
	}

	public BigDecimal getApprovingUserInitiatePrice() {
		return approvingUserInitiatePrice;
	}

	public void setApprovingUserInitiatePrice(BigDecimal approvingUserInitiatePrice) {
		this.approvingUserInitiatePrice = approvingUserInitiatePrice;
	}

	public void setHalfDayTimeIntervals(List<TimeIntervalDTO> halfDayTimeIntervals) {
		this.halfDayTimeIntervals = halfDayTimeIntervals;
	}

	public Byte getUserPriceType() {
		return userPriceType;
	}

	public void setUserPriceType(Byte userPriceType) {
		this.userPriceType = userPriceType;
	}

	public BigDecimal getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}

	public BigDecimal getOrgMemberOriginalPrice() {
		return orgMemberOriginalPrice;
	}

	public void setOrgMemberOriginalPrice(BigDecimal orgMemberOriginalPrice) {
		this.orgMemberOriginalPrice = orgMemberOriginalPrice;
	}

	public BigDecimal getApprovingUserOriginalPrice() {
		return approvingUserOriginalPrice;
	}

	public void setApprovingUserOriginalPrice(BigDecimal approvingUserOriginalPrice) {
		this.approvingUserOriginalPrice = approvingUserOriginalPrice;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
}
