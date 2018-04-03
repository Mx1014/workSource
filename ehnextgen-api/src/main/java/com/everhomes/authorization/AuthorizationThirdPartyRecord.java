// @formatter:off
package com.everhomes.authorization;

import com.everhomes.server.schema.tables.pojos.EhAuthorizationThirdPartyRecords;
import com.everhomes.util.StringHelper;

public class AuthorizationThirdPartyRecord extends EhAuthorizationThirdPartyRecords {
	
	private static final long serialVersionUID = -7403916821674120752L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}