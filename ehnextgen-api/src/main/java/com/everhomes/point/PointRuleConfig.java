// @formatter:off
package com.everhomes.point;

import com.everhomes.server.schema.tables.pojos.EhPointRuleConfigs;
import com.everhomes.util.StringHelper;

public class PointRuleConfig extends EhPointRuleConfigs {
	
	private static final long serialVersionUID = 408459484484945939L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}