// @formatter:off
package com.everhomes.enterprisemoment;

import com.everhomes.server.schema.tables.pojos.EhEnterpriseMoments;
import com.everhomes.util.StringHelper;

public class EnterpriseMoment extends EhEnterpriseMoments {
	
	private static final long serialVersionUID = -7967154121633573531L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}