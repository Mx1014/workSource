package com.everhomes.techpark.punch;

import com.everhomes.server.schema.tables.pojos.EhPunchWorkday;
import com.everhomes.util.StringHelper;

public class PunchWorkday extends EhPunchWorkday {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8851098879719749125L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
