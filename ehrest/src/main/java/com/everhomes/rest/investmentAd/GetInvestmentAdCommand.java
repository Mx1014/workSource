package com.everhomes.rest.investmentAd;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>AdvertisementId: 招商广告id</li>
 * </ul>
 */
public class GetInvestmentAdCommand {
	
	private Long AdvertisementId;

	public Long getAdvertisementId() {
		return AdvertisementId;
	}

	public void setAdvertisementId(Long advertisementId) {
		AdvertisementId = advertisementId;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
