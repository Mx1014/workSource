package com.everhomes.rest.advertisement;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>advertisementOrders: 排序数组</li>
 * </ul>
 */
public class ChangeAdvertisementOrderCommand {
	@ItemType(AdvertisementOrderDTO.class)
	private List<AdvertisementOrderDTO> advertisementOrders;

	public List<AdvertisementOrderDTO> getAdvertisementOrders() {
		return advertisementOrders;
	}

	public void setAdvertisementOrders(List<AdvertisementOrderDTO> advertisementOrders) {
		this.advertisementOrders = advertisementOrders;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
