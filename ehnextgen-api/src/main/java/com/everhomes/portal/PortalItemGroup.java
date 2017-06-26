// @formatter:off
package com.everhomes.portal;

import com.everhomes.server.schema.tables.pojos.EhPortalItemGroups;
import com.everhomes.util.StringHelper;

public class PortalItemGroup extends EhPortalItemGroups {
	
	private static final long serialVersionUID = -6152611441410241533L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}