// @formatter:off
package com.everhomes.portal;

import com.everhomes.server.schema.tables.pojos.EhPortalLayouts;
import com.everhomes.util.StringHelper;

public class PortalLayout extends EhPortalLayouts {
	
	private static final long serialVersionUID = -4190234278585927333L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}