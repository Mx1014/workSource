package com.everhomes.journal;

import com.everhomes.server.schema.tables.pojos.EhJournalConfigs;
import com.everhomes.util.StringHelper;

public class JournalConfig extends EhJournalConfigs{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
