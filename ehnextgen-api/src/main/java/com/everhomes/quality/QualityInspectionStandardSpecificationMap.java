package com.everhomes.quality;

import com.everhomes.server.schema.tables.pojos.EhQualityInspectionStandardSpecificationMap;
import com.everhomes.util.StringHelper;

public class QualityInspectionStandardSpecificationMap extends
		EhQualityInspectionStandardSpecificationMap {

	private static final long serialVersionUID = 381180942296910761L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
