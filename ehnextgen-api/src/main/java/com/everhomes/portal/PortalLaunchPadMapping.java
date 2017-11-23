// @formatter:off
package com.everhomes.portal;

import com.everhomes.server.schema.tables.pojos.EhPortalLaunchPadMappings;
import com.everhomes.util.StringHelper;

public class PortalLaunchPadMapping extends EhPortalLaunchPadMappings {
	
	private static final long serialVersionUID = -5747324503203573818L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}