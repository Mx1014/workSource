// @formatter:off
package com.everhomes.building;

import com.everhomes.server.schema.tables.pojos.EhBuildings;
import com.everhomes.util.StringHelper;

public class Building extends EhBuildings {

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}