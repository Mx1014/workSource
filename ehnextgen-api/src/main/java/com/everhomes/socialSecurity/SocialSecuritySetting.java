// @formatter:off
package com.everhomes.socialSecurity;

import com.everhomes.server.schema.tables.pojos.EhSocialSecuritySettings;
import com.everhomes.util.StringHelper;

public class SocialSecuritySetting extends EhSocialSecuritySettings {
	
	private static final long serialVersionUID = 4845419001421233741L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}