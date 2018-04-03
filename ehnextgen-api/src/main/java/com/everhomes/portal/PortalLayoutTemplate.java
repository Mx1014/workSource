// @formatter:off
package com.everhomes.portal;

import com.everhomes.server.schema.tables.pojos.EhPortalLayoutTemplates;
import com.everhomes.util.StringHelper;

public class PortalLayoutTemplate extends EhPortalLayoutTemplates {
	
	private static final long serialVersionUID = 5846175388683175620L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}