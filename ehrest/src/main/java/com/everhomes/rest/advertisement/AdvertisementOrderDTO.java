package com.everhomes.rest.advertisement;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>advertisementId: 招商广告id</li>
 *  <li>defaultOrder: 排序字段</li>
 * </ul>
 */
public class AdvertisementOrderDTO {
	
	private Long advertisementId;
	private Long defaultOrder;
	
	public Long getAdvertisementId() {
		return advertisementId;
	}
	public void setAdvertisementId(Long advertisementId) {
		this.advertisementId = advertisementId;
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
