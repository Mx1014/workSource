package com.everhomes.rest.rentalv2.admin;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 给资源增加单元格
 * <li>rentalSiteId: 资源id</li>  
 * <li>discountType: 状态，0不打折1满钱减钱优惠 2满天减钱 3 比例 参考{@link com.everhomes.rest.rentalv2.admin.DiscountType}</li> 
 * <li>fullPrice: 满多少钱</li>
 * <li>fullPrice: 减多少钱</li> 
 * </ul>
 */
public class UpdateRentalSiteDiscountAdminCommand {
	@NotNull
	private Long rentalSiteId;
	@NotNull
	private Byte discountType;

	private java.math.BigDecimal fullPrice;
	private java.math.BigDecimal cutprice;
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public Long getRentalSiteId() {
		return rentalSiteId;
	}
	public void setRentalSiteId(Long rentalSiteId) {
		this.rentalSiteId = rentalSiteId;
	}
	public Byte getDiscountType() {
		return discountType;
	}
	public void setDiscountType(Byte discountType) {
		this.discountType = discountType;
	}
	public java.math.BigDecimal getFullPrice() {
		return fullPrice;
	}
	public void setFullPrice(java.math.BigDecimal fullPrice) {
		this.fullPrice = fullPrice;
	}
	public java.math.BigDecimal getCutprice() {
		return cutprice;
	}
	public void setCutprice(java.math.BigDecimal cutprice) {
		this.cutprice = cutprice;
	}
	
}
