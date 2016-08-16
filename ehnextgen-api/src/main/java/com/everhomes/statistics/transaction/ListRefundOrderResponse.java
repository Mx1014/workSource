package com.everhomes.statistics.transaction;

public class ListRefundOrderResponse {

	private String version;
	
	private Integer errorCode;
	
	private RefundOrderResponse response;
	
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

	public RefundOrderResponse getResponse() {
		return response;
	}

	public void setResponse(RefundOrderResponse response) {
		this.response = response;
	}

	
}
