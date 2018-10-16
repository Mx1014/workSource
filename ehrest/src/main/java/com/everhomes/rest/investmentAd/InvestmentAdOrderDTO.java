package com.everhomes.rest.investmentAd;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>advertisementId: 招商广告id</li>
 *  <li>defaultOrder: 排序字段</li>
 * </ul>
 */
public class InvestmentAdOrderDTO {
	
	private Long investmentAdId;
	private Long defaultOrder;
	
	public Long getInvestmentAdId() {
		return investmentAdId;
	}
	public void setInvestmentAdId(Long investmentAdId) {
		this.investmentAdId = investmentAdId;
	}
	public Long getDefaultOrder() {
		return defaultOrder;
	}
	public void setDefaultOrder(Long defaultOrder) {
		this.defaultOrder = defaultOrder;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
