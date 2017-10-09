package com.everhomes.techpark.punch;

import com.everhomes.server.schema.tables.pojos.EhPunchTimeIntervals;
import com.everhomes.util.StringHelper;

public class PunchTimeInterval extends EhPunchTimeIntervals {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 8391455964490212777L; 

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
