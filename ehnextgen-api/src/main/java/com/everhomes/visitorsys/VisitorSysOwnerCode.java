// @formatter:off
package com.everhomes.visitorsys;

import com.everhomes.server.schema.tables.pojos.EhVisitorSysOwnerCode;
import com.everhomes.util.StringHelper;

public class VisitorSysOwnerCode extends EhVisitorSysOwnerCode {
	
	private static final long serialVersionUID = -8468158113545582140L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}