package com.everhomes.quality;

import com.everhomes.server.schema.tables.pojos.EhQualityInspectionLogs;
import com.everhomes.util.StringHelper;

public class QualityInspectionLogs extends EhQualityInspectionLogs{
	
	private static final long serialVersionUID = -4472864515831552908L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
