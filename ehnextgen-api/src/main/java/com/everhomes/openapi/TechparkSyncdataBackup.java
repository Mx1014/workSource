// @formatter:off
package com.everhomes.openapi;

import com.everhomes.server.schema.tables.pojos.EhTechparkSyncdataBackup;
import com.everhomes.util.StringHelper;

public class TechparkSyncdataBackup extends EhTechparkSyncdataBackup {

	private static final long serialVersionUID = 313846426557568142L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}