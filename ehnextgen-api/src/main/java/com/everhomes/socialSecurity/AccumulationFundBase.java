// @formatter:off
package com.everhomes.socialSecurity;

import com.everhomes.server.schema.tables.pojos.EhAccumulationFundBases;
import com.everhomes.util.StringHelper;

public class AccumulationFundBase extends EhAccumulationFundBases {
	
	private static final long serialVersionUID = -4119588874021872578L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}