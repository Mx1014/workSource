// @formatter:off
package com.everhomes.socialSecurity;

import com.everhomes.server.schema.tables.pojos.EhSocialSecurityDepartmentSummary;
import com.everhomes.util.StringHelper;

public class SocialSecurityDepartmentSummary extends EhSocialSecurityDepartmentSummary {
	
	private static final long serialVersionUID = -5259746406711505097L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}