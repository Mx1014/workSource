// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.server.schema.tables.pojos.EhStatEventPortalConfigs;
import com.everhomes.util.StringHelper;

public class StatEventPortalConfig extends EhStatEventPortalConfigs {
	
	private static final long serialVersionUID = -2050966111931396711L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}