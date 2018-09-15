package com.everhomes.rest.investmentAd;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>advertisementOrders: 排序数组</li>
 * </ul>
 */
public class ChangeInvestmentAdOrderCommand {
	@ItemType(InvestmentAdOrderDTO.class)
	private List<InvestmentAdOrderDTO> investmentAdOrders;

	public List<InvestmentAdOrderDTO> getInvestmentAdOrders() {
		return investmentAdOrders;
	}
	
	public void setInvestmentAdOrders(List<InvestmentAdOrderDTO> investmentAdOrders) {
		this.investmentAdOrders = investmentAdOrders;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
