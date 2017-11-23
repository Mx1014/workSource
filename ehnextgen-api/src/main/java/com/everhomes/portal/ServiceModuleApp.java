// @formatter:off
package com.everhomes.portal;

import com.everhomes.server.schema.tables.pojos.EhServiceModuleApps;
import com.everhomes.util.StringHelper;

public class ServiceModuleApp extends EhServiceModuleApps {
	
	private static final long serialVersionUID = -4427568958167322339L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}