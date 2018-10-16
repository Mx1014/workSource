package com.everhomes.investmentAd;

import com.everhomes.server.schema.tables.pojos.EhInvestmentAdvertisements;
import com.everhomes.util.StringHelper;

public class InvestmentAd extends EhInvestmentAdvertisements{

	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
