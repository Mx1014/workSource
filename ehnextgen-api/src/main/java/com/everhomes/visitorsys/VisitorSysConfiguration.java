// @formatter:off
package com.everhomes.visitorsys;

import com.everhomes.server.schema.tables.pojos.EhVisitorSysConfigurations;
import com.everhomes.util.StringHelper;

public class VisitorSysConfiguration extends EhVisitorSysConfigurations {
	
	private static final long serialVersionUID = 3447559246137092210L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}