// @formatter:off
package com.everhomes.visitorsys;

import com.everhomes.server.schema.tables.pojos.EhVisitorSysMessageReceivers;
import com.everhomes.util.StringHelper;

public class VisitorSysMessageReceiver extends EhVisitorSysMessageReceivers {
	
	private static final long serialVersionUID = 2385727615850987181L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}