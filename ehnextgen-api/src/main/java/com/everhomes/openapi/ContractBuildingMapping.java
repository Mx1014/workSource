// @formatter:off
package com.everhomes.openapi;

import com.everhomes.server.schema.tables.pojos.EhContractBuildingMappings;
import com.everhomes.util.StringHelper;

public class ContractBuildingMapping extends EhContractBuildingMappings {

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}