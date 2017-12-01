// @formatter:off
package com.everhomes.point;

import com.everhomes.server.schema.tables.pojos.EhPointEventLogs;
import com.everhomes.util.StringHelper;

public class PointEventLog extends EhPointEventLogs {
	
	private static final long serialVersionUID = 1612038090341917312L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}