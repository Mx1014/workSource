package com.everhomes.xfyun;

import com.everhomes.rest.launchpadbase.AppContext;
import com.everhomes.rest.xfyun.AiUiIntentDTO;
import com.everhomes.util.StringHelper;
import com.everhomes.xfyun.XfyunMatch;

public class RouterQueryParamCommand {
	private Long userId;
	private AppContext context; 
	private AiUiIntentDTO intent;
	private String verifyText;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public AppContext getContext() {
		return context;
	}

	public void setContext(AppContext context) {
		this.context = context;
	}

	public String getVerifyText() {
		return verifyText;
	}

	public void setVerifyText(String verifyText) {
		this.verifyText = verifyText;
	}

	public AiUiIntentDTO getIntent() {
		return intent;
	}

	public void setIntent(AiUiIntentDTO intent) {
		this.intent = intent;
	}
}
