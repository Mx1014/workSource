// @formatter:off
package com.everhomes.socialSecurity;

import com.everhomes.server.schema.tables.pojos.EhSocialSecurityBases;
import com.everhomes.util.StringHelper;

public class SocialSecurityBase extends EhSocialSecurityBases {
	
	private static final long serialVersionUID = 624707583610224832L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}