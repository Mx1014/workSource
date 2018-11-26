package com.everhomes.rest.xfyun;

import com.everhomes.rest.launchpadbase.AppContext;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>context: 需要的广场等参数 {@link com.everhomes.rest.launchpadbase.AppContext}</li>
 * </ul>
 */
public class QueryRoutersCommand {
	private AppContext context; 

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public AppContext getContext() {
		return context;
	}

	public void setContext(AppContext context) {
		this.context = context;
	}

}
