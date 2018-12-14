package com.everhomes.officecubicle;

import com.everhomes.server.schema.tables.pojos.EhOfficeCubicleRefundRule;
import com.everhomes.server.schema.tables.pojos.EhOfficeCubicleRefundTips;
import com.everhomes.util.StringHelper;

public class OfficeCubicleRefundTips extends EhOfficeCubicleRefundTips {





	/**
	 * 
	 */
	private static final long serialVersionUID = 4119254596651245254L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
