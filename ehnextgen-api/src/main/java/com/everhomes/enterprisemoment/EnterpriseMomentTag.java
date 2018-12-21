// @formatter:off
package com.everhomes.enterprisemoment;

import com.everhomes.server.schema.tables.pojos.EhEnterpriseMomentTags;
import com.everhomes.util.StringHelper;

public class EnterpriseMomentTag extends EhEnterpriseMomentTags {
	
	private static final long serialVersionUID = -6417527077048997692L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}