package com.everhomes.rest.videoconf;


import java.math.BigDecimal;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>confCapacity: 账号类型 0-25方 1-100方 2-6方 3-50方 </li>
 *  <li>singleAccountPrice: 正常价格</li>
 *  <li>multipleAccountThreshold: 多账号起售数量 </li>
 *  <li>multipleAccountPrice: 多账号购买价格 </li>
 *  <li>minPeriod: 最少购买月份 </li>
 * </ul>
 *
 */
public class ConfCategoryDTO {
	
	private Byte confCapacity;
	
	private BigDecimal singleAccountPrice;
	
	private int multipleAccountThreshold;
	
	private BigDecimal multipleAccountPrice;
	
	private int minPeriod;
	
	public Byte getConfCapacity() {
		return confCapacity;
	}

	public void setConfCapacity(Byte confCapacity) {
		this.confCapacity = confCapacity;
	}

	public BigDecimal getSingleAccountPrice() {
		return singleAccountPrice;
	}

	public void setSingleAccountPrice(BigDecimal singleAccountPrice) {
		this.singleAccountPrice = singleAccountPrice;
	}

	public int getMultipleAccountThreshold() {
		return multipleAccountThreshold;
	}

	public void setMultipleAccountThreshold(int multipleAccountThreshold) {
		this.multipleAccountThreshold = multipleAccountThreshold;
	}

	public BigDecimal getMultipleAccountPrice() {
		return multipleAccountPrice;
	}

	public void setMultipleAccountPrice(BigDecimal multipleAccountPrice) {
		this.multipleAccountPrice = multipleAccountPrice;
	}

	public int getMinPeriod() {
		return minPeriod;
	}

	public void setMinPeriod(int minPeriod) {
		this.minPeriod = minPeriod;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }	
}
