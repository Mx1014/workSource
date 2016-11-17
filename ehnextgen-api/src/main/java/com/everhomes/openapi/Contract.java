// @formatter:off
package com.everhomes.openapi;

import com.everhomes.server.schema.tables.pojos.EhContracts;
import com.everhomes.util.StringHelper;

public class Contract extends EhContracts {

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}