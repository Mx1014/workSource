package com.everhomes.organization.pm.reportForm;

import com.everhomes.server.schema.tables.pojos.EhPropertyStatisticBuilding;
import com.everhomes.util.StringHelper;

public class BuildingStatistics extends EhPropertyStatisticBuilding{

	private static final long serialVersionUID = 1L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	

}
