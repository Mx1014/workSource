// @formatter:off
package com.everhomes.openapi;

import com.everhomes.server.schema.tables.pojos.EhTechparkSyncdataBackup;
import com.everhomes.util.StringHelper;

public class TechparkSyncdataBackup extends EhTechparkSyncdataBackup {

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}