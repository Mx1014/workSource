package com.everhomes.user;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * 	<li>paramUrl</li>
 * </ul>
 * @author Administrator
 *
 */
public class GetSignatureCommandResponse {

	private String paramUrl;
	
	public String getParamUrl() {
		return paramUrl;
	}

	public void setParamUrl(String paramUrl) {
		this.paramUrl = paramUrl;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
