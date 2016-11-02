package com.everhomes.energy;

import com.everhomes.server.schema.tables.pojos.EhEnergyYearStatistics;
import com.everhomes.util.StringHelper;

public class EnergyYearStatistic extends EhEnergyYearStatistics {

	/**
	 * 
	 */
	private static final long serialVersionUID = -812412286568984424L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
