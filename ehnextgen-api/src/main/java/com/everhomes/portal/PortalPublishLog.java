// @formatter:off
package com.everhomes.portal;

import com.everhomes.server.schema.tables.pojos.EhPortalPublishLogs;
import com.everhomes.util.StringHelper;

public class PortalPublishLog extends EhPortalPublishLogs {
	
	private static final long serialVersionUID = -3457277923114424768L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}