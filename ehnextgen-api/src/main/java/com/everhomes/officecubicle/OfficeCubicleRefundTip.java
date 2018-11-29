package com.everhomes.officecubicle;

import com.everhomes.server.schema.tables.pojos.EhOfficeCubicleRefundTip;
import com.everhomes.util.StringHelper;

public class OfficeCubicleRefundTip extends EhOfficeCubicleRefundTip {



	/**
	 * 
	 */
	private static final long serialVersionUID = 4119254596651245254L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
