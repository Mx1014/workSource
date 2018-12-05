package com.everhomes.rest.parking.handler.haikangweishi;

import org.apache.commons.lang.StringUtils;

import com.everhomes.util.StringHelper;

public class HkwsThirdResponse {
	private Integer errorCode;
	private String errorMessage;
	private String data;
	
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
    
    public boolean isEmpty() {
    	return StringUtils.isBlank(data);
    }

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
