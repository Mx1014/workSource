package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

public class ListUserCommand {
	String keyword;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	
	

}
