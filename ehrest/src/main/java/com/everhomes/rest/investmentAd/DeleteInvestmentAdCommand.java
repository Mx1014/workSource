package com.everhomes.rest.investmentAd;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>advertisementId: 招商广告id</li>
 * </ul>
 */
public class DeleteInvestmentAdCommand {
	
	private Long advertisementId;

	public Long getAdvertisementId() {
		return advertisementId;
	}

	public void setAdvertisementId(Long advertisementId) {
		this.advertisementId = advertisementId;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
