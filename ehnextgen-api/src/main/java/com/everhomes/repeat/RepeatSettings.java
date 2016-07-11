package com.everhomes.repeat;

import com.everhomes.server.schema.tables.pojos.EhRepeatSettings;
import com.everhomes.util.StringHelper;

public class RepeatSettings extends EhRepeatSettings {

	private static final long serialVersionUID = 7391444102316571668L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
