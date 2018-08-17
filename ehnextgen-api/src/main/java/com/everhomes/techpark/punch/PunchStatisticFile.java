// @formatter:off
package com.everhomes.techpark.punch;

import com.everhomes.server.schema.tables.pojos.EhPunchStatisticFiles;
import com.everhomes.util.StringHelper;

public class PunchStatisticFile extends EhPunchStatisticFiles {
	
	private static final long serialVersionUID = 8313887128544787204L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}