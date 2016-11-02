package com.everhomes.energy;

import com.everhomes.server.schema.tables.pojos.EhEnergyYoyStatistics;
import com.everhomes.util.StringHelper;

public class EnergyYoyStatistic extends EhEnergyYoyStatistics {
    /**
	 * 
	 */
	private static final long serialVersionUID = 7600187874066022533L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
