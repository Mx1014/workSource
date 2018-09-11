// @formatter:off
package com.everhomes.techpark.punch;

import com.everhomes.server.schema.tables.pojos.EhPunchDayLogFiles;
import com.everhomes.util.StringHelper;

public class PunchDayLogFile extends EhPunchDayLogFiles {
	
	private static final long serialVersionUID = -985543214608642288L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}