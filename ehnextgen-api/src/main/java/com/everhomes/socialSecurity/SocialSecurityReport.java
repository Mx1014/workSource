// @formatter:off
package com.everhomes.socialSecurity;

import com.everhomes.server.schema.tables.pojos.EhSocialSecurityReport;
import com.everhomes.util.StringHelper;

public class SocialSecurityReport extends EhSocialSecurityReport {
	
	private static final long serialVersionUID = -8260921636834038843L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}