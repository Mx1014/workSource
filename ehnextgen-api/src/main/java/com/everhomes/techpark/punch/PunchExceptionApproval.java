package com.everhomes.techpark.punch;

import com.everhomes.server.schema.tables.pojos.EhPunchExceptionApprovals;
import com.everhomes.util.StringHelper;

public class PunchExceptionApproval extends EhPunchExceptionApprovals {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3585218118635143715L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
