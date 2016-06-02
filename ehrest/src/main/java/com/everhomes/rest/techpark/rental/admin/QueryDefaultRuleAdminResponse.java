package com.everhomes.rest.techpark.rental.admin;

import java.math.BigDecimal;
import java.util.List;

import com.everhomes.discover.ItemType;

/**
 * <ul>
 * 查询默认规则
 * <li>exclusiveFlag: 是否是独占资源</li>
 * <li>unit: 1整租，0.5可半租</li>
 * <li>autoAssign: 是否需要自动分配资源</li>
 * <li>multiUnit: 是否允许预约多个场所</li>
 * <li>needPay: 是否需要支付</li>
 * <li>multiTimeInterval: 是否允许预约多个时段</li>
 * <li>attachments: 预约需要提交的信息</li>
 * <li>rentalType: 预约类型，参考{@link com.everhomes.rest.techpark.rental.admin.RentalType}</li>
 * <li>rentalEndTime: 至少提前预约时间</li>
 * <li>rentalStartTime: 最多提前预约时间</li>
 * <li>rentalStep: 最短可预约时长</li>
 * <li>timeIntervals: 开放时段</li>
 * <li>beginDate: 开放日期始</li>
 * <li>endDate: 开放日期终</li>
 * <li>openWeekday: 周几开放，字符串0000000每一位分别表示星期日一二三四五六，1代表开放，0代表关闭</li>
 * <li>closeDates: 关闭日期</li>
 * <li>workdayPrice: 工作日价格</li>
 * <li>weekendPrice: 周末价格</li>
 * <li>siteCounts: 可预约个数</li>
 * <li>cancelTime: 至少提前取消时间</li>
 * <li>refundFlag: 是否允许退款</li>
 * <li>refundRatio: 退款比例</li>
 * </ul>
 */
public class QueryDefaultRuleAdminResponse {
	private Byte exclusiveFlag;
	private Double unit;
	private Byte autoAssign;
	private Byte multiUnit;
	private Byte needPay;
	private Byte multiTimeInterval;
	@ItemType(Attachment.class)
	private List<Attachment> attachments;
	private Byte rentalType;
	private Long rentalEndTime;
	private Long rentalStartTime;
	private Integer rentalStep;
	@ItemType(TimeInterval.class)
	private List<TimeInterval> timeIntervals;
	private Long beginDate;
	private Long endDate;
	private String openWeekday;
	@ItemType(Long.class)
	private List<Long> closeDates;
	private BigDecimal workdayPrice;
	private BigDecimal weekendPrice;
	private Double siteCounts;
	private Long cancelTime;
	private Byte refundFlag;
	private Integer refundRatio;

	public Byte getExclusiveFlag() {
		return exclusiveFlag;
	}

	public void setExclusiveFlag(Byte exclusiveFlag) {
		this.exclusiveFlag = exclusiveFlag;
	}

	public Double getUnit() {
		return unit;
	}

	public void setUnit(Double unit) {
		this.unit = unit;
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

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
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

	public Integer getRentalStep() {
		return rentalStep;
	}

	public void setRentalStep(Integer rentalStep) {
		this.rentalStep = rentalStep;
	}

	public List<TimeInterval> getTimeIntervals() {
		return timeIntervals;
	}

	public void setTimeIntervals(List<TimeInterval> timeIntervals) {
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

	public String getOpenWeekday() {
		return openWeekday;
	}

	public void setOpenWeekday(String openWeekday) {
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

	public BigDecimal getWeekendPrice() {
		return weekendPrice;
	}

	public void setWeekendPrice(BigDecimal weekendPrice) {
		this.weekendPrice = weekendPrice;
	}

	public Double getSiteCounts() {
		return siteCounts;
	}

	public void setSiteCounts(Double siteCounts) {
		this.siteCounts = siteCounts;
	}

	public Long getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(Long cancelTime) {
		this.cancelTime = cancelTime;
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

}
