package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

public class BizOrderHolder {
	private Object body;
	
	private Boolean result;

	
	public Object getBody() {
		return body;
	}


	public void setBody(Object body) {
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
