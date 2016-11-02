package com.everhomes.energy;

import com.everhomes.server.schema.tables.pojos.EhEnergyMeterReadingLogs;
import com.everhomes.util.StringHelper;
 
public class EnergyMeterReadingLog extends EhEnergyMeterReadingLogs {
	 /**
	 * 
	 */
	private static final long serialVersionUID = -3841310695179022229L;

	@Override
	    public String toString() {
	        return StringHelper.toJsonString(this);
	    } 
}
