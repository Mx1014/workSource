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
	private List<InvestmentAdOrderDTO> advertisementOrders;

	public List<InvestmentAdOrderDTO> getAdvertisementOrders() {
		return advertisementOrders;
	}

	public void setAdvertisementOrders(List<InvestmentAdOrderDTO> advertisementOrders) {
		this.advertisementOrders = advertisementOrders;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
