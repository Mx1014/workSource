package com.everhomes.rest.videoconf;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>orders: 订单数</li>
 *  <li>months: 包月总数 </li>
 * </ul>
 *
 */
public class CountAccountOrdersAndMonths {
	
	private Integer orders;
	
	private BigDecimal months;

	public Integer getOrders() {
		return orders;
	}

	public void setOrders(Integer orders) {
		this.orders = orders;
	}

	public BigDecimal getMonths() {
		return months;
	}

	public void setMonths(BigDecimal months) {
		this.months = months;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
