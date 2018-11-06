package com.everhomes.investmentAd;

import com.everhomes.server.schema.tables.pojos.EhInvestmentAdvertisementAssets;
import com.everhomes.util.StringHelper;

public class InvestmentAdAsset extends EhInvestmentAdvertisementAssets{

	private static final long serialVersionUID = 1L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
