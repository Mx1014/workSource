package com.everhomes.incubator;

import com.everhomes.server.schema.tables.pojos.EhIncubatorApplies;
import com.everhomes.util.StringHelper;

public class IncubatorApply extends EhIncubatorApplies{


	private static final long serialVersionUID = -2466906942489158840L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
