package com.everhomes.pmtask;


import com.everhomes.server.schema.tables.pojos.EhPmTasks;
import com.everhomes.util.StringHelper;

public class PmTask extends EhPmTasks {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	} 
}
