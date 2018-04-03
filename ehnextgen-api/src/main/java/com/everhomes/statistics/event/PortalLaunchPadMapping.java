// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.server.schema.tables.pojos.EhPortalLaunchPadMappings;
import com.everhomes.util.StringHelper;

public class PortalLaunchPadMapping extends EhPortalLaunchPadMappings {
	
	private static final long serialVersionUID = 8825108317349277504L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}