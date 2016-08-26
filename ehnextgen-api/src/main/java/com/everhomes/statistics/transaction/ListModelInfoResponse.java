package com.everhomes.statistics.transaction;

import java.util.List;

public class ListModelInfoResponse {

	private String version;
	
	private Integer errorCode;
	
	private List<Model> response;
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public List<Model> getResponse() {
		return response;
	}

	public void setResponse(List<Model> response) {
		this.response = response;
	}

	
	
}
