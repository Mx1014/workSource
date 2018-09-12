package com.everhomes.advertisement;

import com.everhomes.server.schema.tables.pojos.EhInvestmentAdvertisement;
import com.everhomes.util.StringHelper;

public class Advertisement extends EhInvestmentAdvertisement{

	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
