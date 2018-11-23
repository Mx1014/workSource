package com.everhomes.contract;

import com.everhomes.server.schema.tables.pojos.EhContractStatisticCommunities;
import com.everhomes.util.StringHelper;

public class ContractReportformStatisticCommunitys extends EhContractStatisticCommunities{

	private static final long serialVersionUID = 1L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
