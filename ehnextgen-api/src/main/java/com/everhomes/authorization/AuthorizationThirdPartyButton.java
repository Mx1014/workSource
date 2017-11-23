// @formatter:off
package com.everhomes.authorization;

import com.everhomes.server.schema.tables.pojos.EhAuthorizationThirdPartyButtons;
import com.everhomes.util.StringHelper;

public class AuthorizationThirdPartyButton extends EhAuthorizationThirdPartyButtons {
	
	private static final long serialVersionUID = 1021912407467373940L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}