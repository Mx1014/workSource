package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

public class BizOrderHolder {
	private String body;
	
	private Boolean result;


	public String getBody() {
		return body;
	}


	public void setBody(String body) {
		this.body = body;
	}


	public Boolean getResult() {
		return result;
	}


	public void setResult(Boolean result) {
		this.result = result;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
