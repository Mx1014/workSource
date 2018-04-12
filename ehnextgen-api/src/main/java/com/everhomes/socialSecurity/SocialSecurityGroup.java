// @formatter:off
package com.everhomes.socialSecurity;

import com.everhomes.server.schema.tables.pojos.EhSocialSecurityGroups;
import com.everhomes.util.StringHelper;

public class SocialSecurityGroup extends EhSocialSecurityGroups {
	
	private static final long serialVersionUID = -4770359755523343910L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}