// @formatter:off
package com.everhomes.point;

import com.everhomes.server.schema.tables.pojos.EhPointLogs;
import com.everhomes.util.StringHelper;

public class PointLog extends EhPointLogs {
	
	private static final long serialVersionUID = 103668264002307531L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}