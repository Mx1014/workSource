// @formatter:off
package com.everhomes.rest.rentalv2;

import java.math.BigDecimal;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;


/**
 * 
 * 
 * <ul>
 * <li>rentalType: 0按小时，1按半天，2按天，3按半天带晚上，4按月，参考{@link com.everhomes.rest.rentalv2.RentalType}</li>
 * <li>priceType: 0 按时长定价 1 起步价模式</li>
 * <li>minPrice: 最小价格</li>
 * <li>maxPrice: 最大价格</li>
 * <li>discountType: 折扣类型，0不打折，1满减，2满天减，3比例折扣，参考{@link com.everhomes.rest.rentalv2.admin.DiscountType}</li>
 * <li>fullPrice: 满</li>
 * <li>cutPrice: 减</li>
 * <li>discountRatio: 折扣比例</li>
 * <li>pricePackages: 套餐 参考{@link SitePricePackageDTO}</li>
 * </ul>
 */
public class SitePriceRuleDTO {
	private Byte rentalType;
	private Byte priceType;
	private BigDecimal maxPrice;
	private BigDecimal minPrice;
	private String priceStr;
	@ItemType(SitePricePackageDTO.class)
	private List<SitePricePackageDTO> pricePackages;

	public BigDecimal getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(BigDecimal maxPrice) {
		this.maxPrice = maxPrice;
	}

	public BigDecimal getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(BigDecimal minPrice) {
		this.minPrice = minPrice;
	}

	public Byte getRentalType() {
		return rentalType;
	}

	public void setRentalType(Byte rentalType) {
		this.rentalType = rentalType;
	}

	public String getPriceStr() {
		return priceStr;
	}

	public void setPriceStr(String priceStr) {
		this.priceStr = priceStr;
	}

	public List<SitePricePackageDTO> getPricePackages() {
		return pricePackages;
	}

	public void setPricePackages(List<SitePricePackageDTO> pricePackages) {
		this.pricePackages = pricePackages;
	}

	public Byte getPriceType() {
		return priceType;
	}

	public void setPriceType(Byte priceType) {
		this.priceType = priceType;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
