package com.everhomes.quality;

import com.everhomes.server.schema.tables.pojos.EhQualityInspectionCategories;
import com.everhomes.util.StringHelper;

public class QualityInspectionCategories extends EhQualityInspectionCategories {

	private static final long serialVersionUID = -1441890058024136061L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
