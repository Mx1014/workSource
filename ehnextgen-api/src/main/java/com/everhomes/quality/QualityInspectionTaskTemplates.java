package com.everhomes.quality;

import com.everhomes.server.schema.tables.pojos.EhQualityInspectionTaskTemplates;
import com.everhomes.util.StringHelper;

public class QualityInspectionTaskTemplates extends
		EhQualityInspectionTaskTemplates {
	
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
