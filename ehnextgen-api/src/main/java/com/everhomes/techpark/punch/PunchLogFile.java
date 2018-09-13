// @formatter:off
package com.everhomes.techpark.punch;

import com.everhomes.server.schema.tables.pojos.EhPunchLogFiles;
import com.everhomes.util.StringHelper;

public class PunchLogFile extends EhPunchLogFiles {
	
	private static final long serialVersionUID = -416758654242374513L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}