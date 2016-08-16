package com.everhomes.techpark.punch;

import com.everhomes.server.schema.tables.pojos.EhPunchStatistics;
import com.everhomes.util.StringHelper;

public class PunchStatistic extends EhPunchStatistics {

	/**
	 * @author Wuhan
	 */
	private static final long serialVersionUID = 3038821225471012801L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
