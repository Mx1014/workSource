package com.everhomes.pmtask;

import com.everhomes.server.schema.tables.pojos.EhCommunityTaskLogs;
import com.everhomes.util.StringHelper;

public class CommunityTaskLog extends EhCommunityTaskLogs{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
