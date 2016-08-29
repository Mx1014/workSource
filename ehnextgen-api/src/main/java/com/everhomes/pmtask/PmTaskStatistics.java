package com.everhomes.pmtask;

import com.everhomes.server.schema.tables.pojos.EhPmTaskStatistics;
import com.everhomes.util.StringHelper;

public class PmTaskStatistics extends EhPmTaskStatistics{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
