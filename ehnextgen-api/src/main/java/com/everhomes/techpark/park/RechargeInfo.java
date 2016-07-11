package com.everhomes.techpark.park;

import com.everhomes.server.schema.tables.pojos.EhRechargeInfo;
import com.everhomes.util.StringHelper;

public class RechargeInfo extends EhRechargeInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8907693141495396327L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
