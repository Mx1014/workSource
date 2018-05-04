// @formatter:off
package com.everhomes.visitorsys;

import com.everhomes.server.schema.tables.pojos.EhVisitorSysDevices;
import com.everhomes.util.StringHelper;

public class VisitorSysDevice extends EhVisitorSysDevices {
	
	private static final long serialVersionUID = 311157346395014463L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}