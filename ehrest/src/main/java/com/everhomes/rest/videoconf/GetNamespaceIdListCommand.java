package com.everhomes.rest.videoconf;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;


/**
 * 
 * userIdentifier:用户手机号
 *
 */
public class GetNamespaceIdListCommand {
	
	@NotNull
    private String userIdentifier;

	public String getUserIdentifier() {
		return userIdentifier;
	}

	public void setUserIdentifier(String userIdentifier) {
		this.userIdentifier = userIdentifier;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
