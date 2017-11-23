// @formatter:off
package com.everhomes.portal;

import com.everhomes.server.schema.tables.pojos.EhPortalNavigationBars;
import com.everhomes.util.StringHelper;

public class PortalNavigationBar extends EhPortalNavigationBars {
	
	private static final long serialVersionUID = 610407347888549094L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}