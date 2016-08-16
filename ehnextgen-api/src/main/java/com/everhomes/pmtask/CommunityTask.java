package com.everhomes.pmtask;

import com.everhomes.server.schema.tables.pojos.EhCommunityTasks;
import com.everhomes.util.StringHelper;

public class CommunityTask extends EhCommunityTasks{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	} 
}
