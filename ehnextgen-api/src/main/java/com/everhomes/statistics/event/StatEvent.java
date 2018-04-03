// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.server.schema.tables.pojos.EhStatEvents;
import com.everhomes.util.StringHelper;

public class StatEvent extends EhStatEvents {
	
	private static final long serialVersionUID = 4501966520798213434L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}