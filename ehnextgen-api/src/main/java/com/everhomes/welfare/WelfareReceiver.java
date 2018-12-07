// @formatter:off
package com.everhomes.welfare;

import com.everhomes.server.schema.tables.pojos.EhWelfareReceivers;
import com.everhomes.util.StringHelper;

public class WelfareReceiver extends EhWelfareReceivers {
	
	private static final long serialVersionUID = -8851044692328141383L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}