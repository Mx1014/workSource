package com.everhomes.rest.haian;

import com.everhomes.util.StringHelper;

public class EncryptForInsurobotDTO {
	private String url;
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
