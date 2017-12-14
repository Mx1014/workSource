// @formatter:off
package com.everhomes.socialSecurity;

import com.everhomes.server.schema.tables.pojos.EhSocialSecurityPayments;
import com.everhomes.util.StringHelper;

public class SocialSecurityPayment extends EhSocialSecurityPayments {
	
	private static final long serialVersionUID = -3719910387893258486L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}