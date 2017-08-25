// @formatter:off
package com.everhomes.portal;

import com.everhomes.server.schema.tables.pojos.EhPortalItemCategories;
import com.everhomes.util.StringHelper;

public class PortalItemCategry extends EhPortalItemCategories {
	
	private static final long serialVersionUID = 5397131672340850644L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}