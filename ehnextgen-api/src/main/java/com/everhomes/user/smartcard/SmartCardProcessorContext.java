package com.everhomes.user.smartcard;

import java.util.HashMap;
import java.util.Map;

public class SmartCardProcessorContext {
	private Long userId;
	private Map<String, String> meta;
	
	public SmartCardProcessorContext() {
		meta  = new HashMap<String, String>();
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Map<String, String> getMeta() {
		return meta;
	}

	public void setMeta(Map<String, String> meta) {
		this.meta = meta;
	}

}
