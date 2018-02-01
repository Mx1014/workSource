// @formatter:off
package com.everhomes.socialSecurity;

import com.everhomes.server.schema.tables.pojos.EhSocialSecurityPaymentLogs;
import com.everhomes.util.StringHelper;

public class SocialSecurityPaymentLog extends EhSocialSecurityPaymentLogs {
	
	private static final long serialVersionUID = 3366954133570546603L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}