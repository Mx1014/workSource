package com.everhomes.quality;

import com.everhomes.server.schema.tables.pojos.EhQualityInspectionSpecificationItemResults;
import com.everhomes.util.StringHelper;

public class QualityInspectionSpecificationItemResults extends
		EhQualityInspectionSpecificationItemResults {

	private static final long serialVersionUID = 5635623148015204920L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
