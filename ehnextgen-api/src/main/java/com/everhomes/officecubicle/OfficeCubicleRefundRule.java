package com.everhomes.officecubicle;

import com.everhomes.server.schema.tables.pojos.EhOfficeCubicleRefundRule;
import com.everhomes.util.StringHelper;

public class OfficeCubicleRefundRule extends EhOfficeCubicleRefundRule {



	/**
	 * 
	 */
	private static final long serialVersionUID = 6326121964176465474L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
