package com.everhomes.statistics.transaction;


import java.util.List;

public class ListBusinessInfoResponse {

	private String version;
	
	private Integer errorCode;

	private List<BizBusinessInfo> response;

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

	public List<BizBusinessInfo> getResponse() {
		return response;
	}

	public void setResponse(List<BizBusinessInfo> response) {
		this.response = response;
	}
}
