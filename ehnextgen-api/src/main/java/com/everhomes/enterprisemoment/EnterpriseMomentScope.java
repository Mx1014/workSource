// @formatter:off
package com.everhomes.enterprisemoment;

import com.everhomes.server.schema.tables.pojos.EhEnterpriseMomentScopes;
import com.everhomes.util.StringHelper;

public class EnterpriseMomentScope extends EhEnterpriseMomentScopes {
	
	private static final long serialVersionUID = 1504638777925184926L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}