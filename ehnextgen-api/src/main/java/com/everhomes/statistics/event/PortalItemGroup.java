// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.server.schema.tables.pojos.EhPortalItemGroups;
import com.everhomes.util.StringHelper;

public class PortalItemGroup extends EhPortalItemGroups {
	
	private static final long serialVersionUID = 4333390250716261476L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}