// @formatter:off
package com.everhomes.visitorsys;

import com.everhomes.server.schema.tables.pojos.EhVisitorSysOfficeLocations;
import com.everhomes.util.StringHelper;

public class VisitorSysOfficeLocation extends EhVisitorSysOfficeLocations {
	
	private static final long serialVersionUID = 640785830563682254L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}