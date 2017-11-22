// @formatter:off
package com.everhomes.point;

import com.everhomes.server.schema.tables.pojos.EhPointModules;
import com.everhomes.util.StringHelper;

public class PointModule extends EhPointModules {
	
	private static final long serialVersionUID = -9182521578030591677L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}