package com.everhomes.rest.techpark.rental.admin;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 添加默认规则
 * <li>ownerType: 所有者类型 参考
 * {@link com.everhomes.rest.techpark.rental.RentalOwnerType}</li>
 * <li>ownerId: 园区id</li>
 * <li>resourceTypeId: 图标id</li>
 * <li>exclusiveFlag: 是否是独占资源</li>
 * <li>unit: 1整租，0.5可半租</li>
 * <li>autoAssign: 是否需要自动分配资源</li>
 * <li>multiUnit: 是否允许预约多个场所</li>
 * <li>needPay: 是否需要支付</li>
 * <li>multiTimeInterval: 是否允许预约多个时段</li>
 * <li>attachments: 预约需要提交的信息</li>
 * <li>rentalType: 预约类型，参考{@link com.everhomes.rest.techpark.rental.RentalType}</li>
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
public class DefaultRuleDTO {
	private Long id;
	@NotNull
	private String ownerType;
	@NotNull
	private Long ownerId;
	@NotNull
	private Long resourceTypeId;
	private Byte exclusiveFlag;
	private Double unit;
	private Byte autoAssign;
	private Byte multiUnit;
	private Byte needPay;
	private Byte multiTimeInterval;
	@ItemType(AttachmentConfigDTO.class)
	private List<AttachmentConfigDTO> attachments;
	private Byte rentalType;
	private Long rentalEndTime;
	private Long rentalStartTime;
	private Integer rentalStep;
	@ItemType(TimeIntervalDTO.class)
	private List<TimeIntervalDTO> timeIntervals;
	private Long beginDate;
	private Long endDate;

	@ItemType(Integer.class)
	private List<Integer> openWeekday;
	@ItemType(Long.class)
	private List<Long> closeDates;
	private BigDecimal workdayPrice;
	private BigDecimal weekendPrice;
	private Double siteCounts;
	private Long cancelTime;
	private Byte refundFlag;
	private Integer refundRatio;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
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

	public List<AttachmentConfigDTO> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<AttachmentConfigDTO> attachments) {
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

}
