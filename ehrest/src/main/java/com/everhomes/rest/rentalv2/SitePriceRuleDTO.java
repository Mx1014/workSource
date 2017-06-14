// @formatter:off
package com.everhomes.rest.rentalv2;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;


/**
 * 
 * 
 * <ul>
 * <li>rentalType: 0按小时，1按半天，2按天，3按半天带晚上，4按月，参考{@link com.everhomes.rest.rentalv2.RentalType}</li>
 * <li>minPrice: 最小价格</li>
 * <li>maxPrice: 最大价格</li>
 * <li>discountType: 折扣类型，0不打折，1满减，2满天减，3比例折扣，参考{@link com.everhomes.rest.rentalv2.admin.DiscountType}</li>
 * <li>fullPrice: 满</li>
 * <li>cutPrice: 减</li>
 * <li>discountRatio: 折扣比例</li>
 * </ul>
 */
public class SitePriceRuleDTO {
	private Byte rentalType;
	private BigDecimal minPrice;
	private BigDecimal maxPrice;
	private Byte discountType;
	private BigDecimal fullPrice;
	private BigDecimal cutPrice;
	private Double discountRatio;

	public Byte getRentalType() {
		return rentalType;
	}

	public void setRentalType(Byte rentalType) {
		this.rentalType = rentalType;
	}

	public BigDecimal getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(BigDecimal minPrice) {
		this.minPrice = minPrice;
	}

	public BigDecimal getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(BigDecimal maxPrice) {
		this.maxPrice = maxPrice;
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
