package com.everhomes.contract;

import com.everhomes.server.schema.tables.pojos.EhPropertyStatisticCommunity;
import com.everhomes.util.StringHelper;

public class CommunityStatistics extends EhPropertyStatisticCommunity{

	private static final long serialVersionUID = 1L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
