package com.everhomes.wanke;

import com.alibaba.fastjson.JSONObject;

public class MashenToken {
	private String accessToken;
	private String jsapiToken;
	
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getJsapiToken() {
		return jsapiToken;
	}
	public void setJsapiToken(String jsapiToken) {
		this.jsapiToken = jsapiToken;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
	
}
