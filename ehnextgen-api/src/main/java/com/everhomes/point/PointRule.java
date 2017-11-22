// @formatter:off
package com.everhomes.point;

import com.everhomes.server.schema.tables.pojos.EhPointRules;
import com.everhomes.util.StringHelper;

public class PointRule extends EhPointRules {
	
	private static final long serialVersionUID = -1505120444134352970L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}