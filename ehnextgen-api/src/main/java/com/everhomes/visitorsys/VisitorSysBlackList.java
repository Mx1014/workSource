// @formatter:off
package com.everhomes.visitorsys;

import com.everhomes.server.schema.tables.pojos.EhVisitorSysBlackList;
import com.everhomes.util.StringHelper;

public class VisitorSysBlackList extends EhVisitorSysBlackList {
	
	private static final long serialVersionUID = 2879409821840886537L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}