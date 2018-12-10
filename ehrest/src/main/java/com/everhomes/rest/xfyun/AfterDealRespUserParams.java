package com.everhomes.rest.xfyun;

import com.everhomes.rest.launchpadbase.AppContext;
import com.everhomes.util.StringHelper;

public class AfterDealRespUserParams {
	private AppContext context;
	private String token;

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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	} 


}
