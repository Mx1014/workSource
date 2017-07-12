package com.everhomes.energy;

import com.everhomes.server.schema.tables.pojos.EhEnergyCountStatistics;
import com.everhomes.util.StringHelper;

public class EnergyCountStatistic extends EhEnergyCountStatistics {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7916970141179376522L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
