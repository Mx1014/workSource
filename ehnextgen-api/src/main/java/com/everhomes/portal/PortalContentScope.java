// @formatter:off
package com.everhomes.portal;

import com.everhomes.server.schema.tables.pojos.EhPortalContentScopes;
import com.everhomes.util.StringHelper;

public class PortalContentScope extends EhPortalContentScopes {
	
	private static final long serialVersionUID = -1801656085801701802L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}