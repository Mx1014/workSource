package com.everhomes.incubator;

import com.everhomes.server.schema.tables.pojos.EhIncubatorProjectTypes;
import com.everhomes.util.StringHelper;

public class IncubatorProjectType extends EhIncubatorProjectTypes{


	private static final long serialVersionUID = -901737933886695308L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
