// @formatter:off
package com.everhomes.yellowPage;

import com.everhomes.server.schema.tables.pojos.EhServiceAllianceComments;
import com.everhomes.util.StringHelper;

public class ServiceAllianceComment extends EhServiceAllianceComments {
	
	private static final long serialVersionUID = 3224647444903999185L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}