// @formatter:off
package com.everhomes.openapi;

import com.everhomes.server.schema.tables.pojos.EhTechparkSyncdataLogs;
import com.everhomes.util.StringHelper;

public class TechparkSyncdataLog extends EhTechparkSyncdataLogs {

	private static final long serialVersionUID = -9098711073972321532L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}