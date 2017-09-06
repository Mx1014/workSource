// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.server.schema.tables.pojos.EhPortalNavigationBars;
import com.everhomes.util.StringHelper;

public class PortalNavigationBar extends EhPortalNavigationBars {
	
	private static final long serialVersionUID = 4431777537211541838L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}