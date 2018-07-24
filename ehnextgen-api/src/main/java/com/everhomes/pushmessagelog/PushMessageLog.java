package com.everhomes.pushmessagelog;

import com.everhomes.server.schema.tables.pojos.EhPushMessageLog;
import com.everhomes.util.StringHelper;

public class PushMessageLog  extends EhPushMessageLog{

	private static final long serialVersionUID = 6227170794943349001L;

	@Override
	public String toString() {
		   return StringHelper.toJsonString(this);
	}

}
