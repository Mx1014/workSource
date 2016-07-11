package com.everhomes.quality;

import com.everhomes.server.schema.tables.pojos.EhQualityInspectionEvaluations;
import com.everhomes.util.StringHelper;

public class QualityInspectionEvaluations extends
		EhQualityInspectionEvaluations {

	private static final long serialVersionUID = 7513208314837663387L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
