// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.server.schema.tables.pojos.EhStatEventDeviceLogs;
import com.everhomes.util.StringHelper;

public class StatEventDeviceLog extends EhStatEventDeviceLogs {
	
	private static final long serialVersionUID = -7876451299488520557L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}