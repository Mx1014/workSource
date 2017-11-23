// @formatter:off
package com.everhomes.authorization;

import com.everhomes.server.schema.tables.pojos.EhAuthorizationThirdPartyForms;
import com.everhomes.util.StringHelper;

public class AuthorizationThirdPartyForm extends EhAuthorizationThirdPartyForms {
	
	private static final long serialVersionUID = -5599931954771130474L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}