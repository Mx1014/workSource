// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.server.schema.tables.pojos.EhStatEventParams;
import com.everhomes.util.StringHelper;

public class StatEventParam extends EhStatEventParams {
	
	private static final long serialVersionUID = 7013857199431154414L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}