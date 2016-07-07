package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

public class GetActivityShareDetailCommand {
	
	private String postToken;
	
	public String getPostToken() {
		return postToken;
	}

	public void setPostToken(String postToken) {
		this.postToken = postToken;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
