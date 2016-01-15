// @formatter:off
// generated at 2015-08-10 11:20:28
package com.everhomes.rest.oauth2;

import com.everhomes.rest.user.UserInfo;

public class GetUserInfoResultResponse {
	public String version;
	public String errorScope;
	public Integer errorCode;
	public String errorDescription;
	public UserInfo response;

	public GetUserInfoResultResponse() {
	}

	public UserInfo getResponse() {
		return response;
	}

	public void setResponse(UserInfo response) {
		this.response = response;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getErrorScope() {
		return errorScope;
	}

	public void setErrorScope(String errorScope) {
		this.errorScope = errorScope;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	
	
}
