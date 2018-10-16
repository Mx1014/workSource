package com.everhomes.rest.user;

import java.util.ArrayList;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class SmartCardVerifyResponse {
	@ItemType(String.class)
	private List<String> verifyResults;
	
	public SmartCardVerifyResponse() {
		verifyResults = new ArrayList<String>();
	}

    public List<String> getVerifyResults() {
		return verifyResults;
	}

	public void setVerifyResults(List<String> verifyResults) {
		this.verifyResults = verifyResults;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
