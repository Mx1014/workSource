// @formatter:off
package com.everhomes.rest.rentalv2.admin;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;

/**
 * 
 * 
 * <ul>
 * <li>rentalType: 0按小时，1按半天，2按天，3按半天带晚上，4按月，参考{@link com.everhomes.rest.rentalv2.RentalType}</li>
 * <li>workdayPrice: 园区客户工作日价</li>
 * <li>weekendPrice: 园区客户节假日价</li>
 * <li>orgMemberWorkdayPrice: 集团内部工作日价</li>
 * <li>orgMemberWeekendPrice: 集团内部节假日价</li>
 * <li>approvingUserWorkdayPrice: 外部客户工作日价</li>
 * <li>approvingUserWeekendPrice: 外部客户节假日价</li>
 * <li>discountType: 折扣类型，0不打折，1满减，2满天减，3比例折扣，参考{@link com.everhomes.rest.rentalv2.admin.DiscountType}</li>
 * <li>fullPrice: 满</li>
 * <li>cutPrice: 减</li>
 * <li>discountRatio: 折扣比例</li>
 * </ul>
 */
public class PriceRuleDTO {
	private Long id;
	private Byte rentalType;
	private BigDecimal workdayPrice;
	private BigDecimal weekendPrice;
	private BigDecimal orgMemberWorkdayPrice;
	private BigDecimal orgMemberWeekendPrice;
	private BigDecimal approvingUserWorkdayPrice;
	private BigDecimal approvingUserWeekendPrice;
	private Byte discountType;
	private BigDecimal fullPrice;
	private BigDecimal cutPrice;
	private Double discountRatio;
	private Long cellBeginId;
	private Long cellEndId;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCellBeginId() {
		return cellBeginId;
	}

	public void setCellBeginId(Long cellBeginId) {
		this.cellBeginId = cellBeginId;
	}

	public Long getCellEndId() {
		return cellEndId;
	}

	public void setCellEndId(Long cellEndId) {
		this.cellEndId = cellEndId;
	}

	public Byte getRentalType() {
		return rentalType;
	}

	public void setRentalType(Byte rentalType) {
		this.rentalType = rentalType;
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

	public BigDecimal getOrgMemberWorkdayPrice() {
		return orgMemberWorkdayPrice;
	}

	public void setOrgMemberWorkdayPrice(BigDecimal orgMemberWorkdayPrice) {
		this.orgMemberWorkdayPrice = orgMemberWorkdayPrice;
	}

	public BigDecimal getOrgMemberWeekendPrice() {
		return orgMemberWeekendPrice;
	}

	public void setOrgMemberWeekendPrice(BigDecimal orgMemberWeekendPrice) {
		this.orgMemberWeekendPrice = orgMemberWeekendPrice;
	}

	public BigDecimal getApprovingUserWorkdayPrice() {
		return approvingUserWorkdayPrice;
	}

	public void setApprovingUserWorkdayPrice(BigDecimal approvingUserWorkdayPrice) {
		this.approvingUserWorkdayPrice = approvingUserWorkdayPrice;
	}

	public BigDecimal getApprovingUserWeekendPrice() {
		return approvingUserWeekendPrice;
	}

	public void setApprovingUserWeekendPrice(BigDecimal approvingUserWeekendPrice) {
		this.approvingUserWeekendPrice = approvingUserWeekendPrice;
	}

	public Byte getDiscountType() {
		return discountType;
	}

	public void setDiscountType(Byte discountType) {
		this.discountType = discountType;
	}

	public BigDecimal getFullPrice() {
		return fullPrice;
	}

	public void setFullPrice(BigDecimal fullPrice) {
		this.fullPrice = fullPrice;
	}

	public BigDecimal getCutPrice() {
		return cutPrice;
	}

	public void setCutPrice(BigDecimal cutPrice) {
		this.cutPrice = cutPrice;
	}

	public Double getDiscountRatio() {
		return discountRatio;
	}

	public void setDiscountRatio(Double discountRatio) {
		this.discountRatio = discountRatio;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
