// @formatter:off
package com.everhomes.welfare;

import com.everhomes.server.schema.tables.pojos.EhWelfarePoints;
import com.everhomes.util.StringHelper;

public class WelfarePoint extends EhWelfarePoints {
	
	private static final long serialVersionUID = -6110913681505699724L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}