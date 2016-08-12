package com.everhomes.journal;

import com.everhomes.server.schema.tables.pojos.EhJournals;
import com.everhomes.util.StringHelper;

public class Journal extends EhJournals{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
