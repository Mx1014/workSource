package com.everhomes.statistics.transaction;

public class ListPaidOrderResponse {

	private String version;
	
	private Integer errorCode;
	
	private PaidOrderResponse response;
	
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

	public PaidOrderResponse getResponse() {
		return response;
	}

	public void setResponse(PaidOrderResponse response) {
		this.response = response;
	}

	
}
