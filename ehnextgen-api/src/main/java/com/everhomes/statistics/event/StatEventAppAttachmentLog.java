// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.server.schema.tables.pojos.EhStatEventAppAttachmentLogs;
import com.everhomes.util.StringHelper;

public class StatEventAppAttachmentLog extends EhStatEventAppAttachmentLogs {
	
	private static final long serialVersionUID = 6409399324550688711L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}