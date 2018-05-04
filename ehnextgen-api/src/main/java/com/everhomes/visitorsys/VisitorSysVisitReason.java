// @formatter:off
package com.everhomes.visitorsys;

import com.everhomes.server.schema.tables.pojos.EhVisitorSysVisitReason;
import com.everhomes.util.StringHelper;

public class VisitorSysVisitReason extends EhVisitorSysVisitReason {
	
	private static final long serialVersionUID = -4821319330983083714L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}