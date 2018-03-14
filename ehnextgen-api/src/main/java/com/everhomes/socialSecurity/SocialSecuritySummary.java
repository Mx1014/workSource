// @formatter:off
package com.everhomes.socialSecurity;

import com.everhomes.server.schema.tables.pojos.EhSocialSecuritySummary;
import com.everhomes.util.StringHelper;

public class SocialSecuritySummary extends EhSocialSecuritySummary {
	
	private static final long serialVersionUID = -3795443382690477727L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}