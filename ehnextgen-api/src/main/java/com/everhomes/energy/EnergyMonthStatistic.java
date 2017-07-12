package com.everhomes.energy;

import com.everhomes.server.schema.tables.pojos.EhEnergyMonthStatistics;
import com.everhomes.util.StringHelper;

public class EnergyMonthStatistic extends EhEnergyMonthStatistics {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6959046359579994794L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
