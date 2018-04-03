package com.everhomes.quality;

import com.everhomes.server.schema.tables.pojos.EhQualityInspectionSpecifications;
import com.everhomes.util.StringHelper;

public class QualityInspectionSpecifications extends
		EhQualityInspectionSpecifications {

	private static final long serialVersionUID = -3104264912481941554L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
