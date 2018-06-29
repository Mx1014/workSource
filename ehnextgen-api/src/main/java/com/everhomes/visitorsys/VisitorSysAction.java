// @formatter:off
package com.everhomes.visitorsys;

import com.everhomes.server.schema.tables.pojos.EhVisitorSysActions;
import com.everhomes.util.StringHelper;

public class VisitorSysAction extends EhVisitorSysActions {
	
	private static final long serialVersionUID = 6934169600790866043L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}