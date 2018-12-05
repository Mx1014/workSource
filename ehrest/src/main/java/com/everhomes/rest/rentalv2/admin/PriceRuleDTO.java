// @formatter:off
package com.everhomes.rest.rentalv2.admin;

import java.math.BigDecimal;
import java.util.List;

import com.everhomes.rest.rentalv2.RentalPriceClassificationDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: id</li>
 * <li>rentalType: 0按小时，1按半天，2按天，3按半天带晚上，4按月，参考{@link com.everhomes.rest.rentalv2.RentalType}</li>
 * <li>priceType: 0 按时长定价 1 起步价模式</li>
 * <li>workdayPrice: 园区客户工作日价</li>
 * <li>initiatePrice: 园区客户起步后价格</li>
 * <li>discountType: 折扣类型，0不打折，1满减，2满天减，3比例折扣，参考{@link com.everhomes.rest.rentalv2.admin.DiscountType}</li>
 * <li>fullPrice: 满</li>
 * <li>cutPrice: 减</li>
 * <li>discountRatio: 折扣比例</li>
 * <li>cellBeginId: cellBeginId</li>
 * <li>cellEndId: cellEndId</li>
 * <li>userPriceType: userPriceType</li>
 * </ul>
 */
public class PriceRuleDTO {
	private Long id;
	private Byte rentalType;
	private Byte priceType;
	private BigDecimal workdayPrice;
	private BigDecimal originalPrice;
	private BigDecimal initiatePrice;
	private Byte discountType;
	private BigDecimal fullPrice;
	private BigDecimal cutPrice;
	private Double discountRatio;
	private Long cellBeginId;
	private Long cellEndId;
	private Byte userPriceType;
	private List<RentalPriceClassificationDTO> classifications;

	public Byte getUserPriceType() {
		return userPriceType;
	}

	public void setUserPriceType(Byte userPriceType) {
		this.userPriceType = userPriceType;
	}

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

	public BigDecimal getInitiatePrice() {
		return initiatePrice;
	}

	public void setInitiatePrice(BigDecimal initiatePrice) {
		this.initiatePrice = initiatePrice;
	}

	public List<RentalPriceClassificationDTO> getClassifications() {
		return classifications;
	}

	public void setClassifications(List<RentalPriceClassificationDTO> classifications) {
		this.classifications = classifications;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public BigDecimal getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}

}
