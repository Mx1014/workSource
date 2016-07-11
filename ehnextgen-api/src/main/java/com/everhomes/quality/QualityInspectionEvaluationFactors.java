package com.everhomes.quality;

import com.everhomes.server.schema.tables.pojos.EhQualityInspectionEvaluationFactors;
import com.everhomes.util.StringHelper;

public class QualityInspectionEvaluationFactors extends
		EhQualityInspectionEvaluationFactors {

	private static final long serialVersionUID = 3567750587969228212L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
