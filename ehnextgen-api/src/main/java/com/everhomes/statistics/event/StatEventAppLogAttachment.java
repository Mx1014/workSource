// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.server.schema.tables.pojos.EhStatEventAppLogAttachments;
import com.everhomes.util.StringHelper;

public class StatEventAppLogAttachment extends EhStatEventAppLogAttachments {
	
	private static final long serialVersionUID = -128629633131360235L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}