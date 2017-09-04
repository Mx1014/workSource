// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.server.schema.tables.pojos.EhPortalLayouts;
import com.everhomes.util.StringHelper;

public class PortalLayout extends EhPortalLayouts {
	
	private static final long serialVersionUID = 4185035423963522020L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}