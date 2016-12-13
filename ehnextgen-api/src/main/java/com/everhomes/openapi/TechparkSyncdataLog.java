// @formatter:off
package com.everhomes.openapi;

import com.everhomes.server.schema.tables.pojos.EhTechparkSyncdataLogs;
import com.everhomes.util.StringHelper;

public class TechparkSyncdataLog extends EhTechparkSyncdataLogs {

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}