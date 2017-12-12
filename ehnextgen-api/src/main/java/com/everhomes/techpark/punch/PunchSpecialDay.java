package com.everhomes.techpark.punch;

import com.everhomes.server.schema.tables.pojos.EhPunchSpecialDays;
import com.everhomes.util.StringHelper;

public class PunchSpecialDay extends EhPunchSpecialDays {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3038821225473212801L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
