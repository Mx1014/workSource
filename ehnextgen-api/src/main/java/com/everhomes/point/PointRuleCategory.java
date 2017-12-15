// @formatter:off
package com.everhomes.point;

import com.everhomes.server.schema.tables.pojos.EhPointRuleCategories;
import com.everhomes.util.StringHelper;

public class PointRuleCategory extends EhPointRuleCategories {
	
	private static final long serialVersionUID = 815115878186322575L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}