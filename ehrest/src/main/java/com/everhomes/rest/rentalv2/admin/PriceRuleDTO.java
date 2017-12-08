// @formatter:off
package com.everhomes.rest.rentalv2.admin;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;

/**
 * 
 * 
 * <ul>
 * <li>rentalType: 0按小时，1按半天，2按天，3按半天带晚上，4按月，参考{@link com.everhomes.rest.rentalv2.RentalType}</li>
 * <li>priceType: 0 按时长定价 1 起步价模式</li>
 * <li>workdayPrice: 园区客户工作日价</li>
 * <li>initiatePrice: 园区客户起步后价格</li>
 * <li>orgMemberWorkdayPrice: 集团内部工作日价</li>
 * <li>orgMemberInitiatePrice: 集团内部起步后价格</li>
 * <li>approvingUserWorkdayPrice: 外部客户工作日价</li>
 * <li>approvingUserInitiatePrice: 外部客户起步后价格</li>
 * <li>discountType: 折扣类型，0不打折，1满减，2满天减，3比例折扣，参考{@link com.everhomes.rest.rentalv2.admin.DiscountType}</li>
 * <li>fullPrice: 满</li>
 * <li>cutPrice: 减</li>
 * <li>discountRatio: 折扣比例</li>
 * <li>orgMemberDiscountType: 集团内部折扣类型，0不打折，1满减，2满天减，3比例折扣，参考{@link com.everhomes.rest.rentalv2.admin.DiscountType}</li>
 * <li>orgMemberFullPrice: 集团内部满</li>
 * <li>orgMemberCutPrice: 集团内部减</li>
 * <li>orgMemberDiscountRatio: 集团内部折扣比例</li>
 * <li>approvingUserDiscountType: 外部客户折扣类型，0不打折，1满减，2满天减，3比例折扣，参考{@link com.everhomes.rest.rentalv2.admin.DiscountType}</li>
 * <li>approvingUserFullPrice: 外部客户满</li>
 * <li>approvingUserCutPrice: 外部客户减</li>
 * <li>approvingUserDiscountRatio: 外部客户折扣比例</li>
 * </ul>
 */
public class PriceRuleDTO {
	private Long id;
	private Byte rentalType;
	private Byte priceType;
	private BigDecimal workdayPrice;
	private BigDecimal initiatePrice;
	private BigDecimal orgMemberWorkdayPrice;
	private BigDecimal orgMemberInitiatePrice;
	private BigDecimal approvingUserWorkdayPrice;
	private BigDecimal approvingUserInitiatePrice;
	private Byte discountType;
	private BigDecimal fullPrice;
	private BigDecimal cutPrice;
	private Double discountRatio;
	private Byte  orgMemberDiscountType;
	private BigDecimal  orgMemberFullPrice;
	private BigDecimal  orgMemberCutPrice;
	private Double  orgMemberDiscountRatio;
	private Byte approvingUserDiscountType;
	private BigDecimal approvingUserFullPrice;
	private BigDecimal approvingUserCutPrice;
	private Double approvingUserDiscountRatio;
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

	public Byte getPriceType() {
		return priceType;
	}

	public void setPriceType(Byte priceType) {
		this.priceType = priceType;
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

	public Byte getOrgMemberDiscountType() {
		return orgMemberDiscountType;
	}

	public void setOrgMemberDiscountType(Byte orgMemberDiscountType) {
		this.orgMemberDiscountType = orgMemberDiscountType;
	}

	public BigDecimal getOrgMemberFullPrice() {
		return orgMemberFullPrice;
	}

	public void setOrgMemberFullPrice(BigDecimal orgMemberFullPrice) {
		this.orgMemberFullPrice = orgMemberFullPrice;
	}

	public BigDecimal getOrgMemberCutPrice() {
		return orgMemberCutPrice;
	}

	public void setOrgMemberCutPrice(BigDecimal orgMemberCutPrice) {
		this.orgMemberCutPrice = orgMemberCutPrice;
	}

	public Double getOrgMemberDiscountRatio() {
		return orgMemberDiscountRatio;
	}

	public void setOrgMemberDiscountRatio(Double orgMemberDiscountRatio) {
		this.orgMemberDiscountRatio = orgMemberDiscountRatio;
	}

	public Byte getApprovingUserDiscountType() {
		return approvingUserDiscountType;
	}

	public void setApprovingUserDiscountType(Byte approvingUserDiscountType) {
		this.approvingUserDiscountType = approvingUserDiscountType;
	}

	public BigDecimal getApprovingUserFullPrice() {
		return approvingUserFullPrice;
	}

	public void setApprovingUserFullPrice(BigDecimal approvingUserFullPrice) {
		this.approvingUserFullPrice = approvingUserFullPrice;
	}

	public BigDecimal getApprovingUserCutPrice() {
		return approvingUserCutPrice;
	}

	public void setApprovingUserCutPrice(BigDecimal approvingUserCutPrice) {
		this.approvingUserCutPrice = approvingUserCutPrice;
	}

	public Double getApprovingUserDiscountRatio() {
		return approvingUserDiscountRatio;
	}

	public void setApprovingUserDiscountRatio(Double approvingUserDiscountRatio) {
		this.approvingUserDiscountRatio = approvingUserDiscountRatio;
	}
}
