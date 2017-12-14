// @formatter:off
package com.everhomes.socialSecurity;

import com.everhomes.server.schema.tables.pojos.EhAccumulationFundSettings;
import com.everhomes.util.StringHelper;

public class AccumulationFundSetting extends EhAccumulationFundSettings {
	
	private static final long serialVersionUID = 3304645951691262587L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}