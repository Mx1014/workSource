// @formatter:off
package com.everhomes.openapi;

import com.everhomes.server.schema.tables.pojos.EhContractBuildingMappings;
import com.everhomes.util.StringHelper;

public class ContractBuildingMapping extends EhContractBuildingMappings {

	private static final long serialVersionUID = 3907617872833595789L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}