package com.everhomes.quality;

import com.everhomes.server.schema.tables.pojos.EhQualityInspectionStandardGroupMap;
import com.everhomes.util.StringHelper;

public class QualityInspectionStandardGroupMap extends
		EhQualityInspectionStandardGroupMap {

	private static final long serialVersionUID = 8996352888347336248L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
