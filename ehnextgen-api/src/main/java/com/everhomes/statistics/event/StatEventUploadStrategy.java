// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.server.schema.tables.pojos.EhStatEventUploadStrategies;
import com.everhomes.util.StringHelper;

public class StatEventUploadStrategy extends EhStatEventUploadStrategies {
	
	private static final long serialVersionUID = -3537911854761526204L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}