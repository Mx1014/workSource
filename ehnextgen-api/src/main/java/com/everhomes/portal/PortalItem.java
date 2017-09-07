// @formatter:off
package com.everhomes.portal;

import com.everhomes.server.schema.tables.pojos.EhPortalItems;
import com.everhomes.util.StringHelper;

public class PortalItem extends EhPortalItems {
	
	private static final long serialVersionUID = -5739623026510920217L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}