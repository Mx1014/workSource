// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.server.schema.tables.pojos.EhPortalItems;
import com.everhomes.util.StringHelper;

public class PortalItem extends EhPortalItems {
	
	private static final long serialVersionUID = 9120998246747101737L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}