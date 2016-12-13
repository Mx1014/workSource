// @formatter:off
package com.everhomes.openapi;

import com.everhomes.server.schema.tables.pojos.EhTechparkSyncdataLog;
import com.everhomes.util.StringHelper;

public class TechparkSyncdataLog extends EhTechparkSyncdataLog {

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}