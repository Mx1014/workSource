// @formatter:off
package com.everhomes.visitorsys;

import com.everhomes.server.schema.tables.pojos.EhVisitorSysVisitors;
import com.everhomes.util.StringHelper;

public class VisitorSysVisitor extends EhVisitorSysVisitors {
	
	private static final long serialVersionUID = 540929539798427587L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}