// @formatter:off
package com.everhomes.socialSecurity;

import com.everhomes.server.schema.tables.pojos.EhSocialSecurityInoutReport;
import com.everhomes.util.StringHelper;

public class SocialSecurityInoutReport extends EhSocialSecurityInoutReport {
	
	private static final long serialVersionUID = -7318450361864711938L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}