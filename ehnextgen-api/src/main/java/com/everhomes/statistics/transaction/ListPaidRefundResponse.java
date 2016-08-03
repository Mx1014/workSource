package com.everhomes.statistics.transaction;

public class ListPaidRefundResponse {

	private String version;
	
	private Integer errorCode;
	
	private PaidRefundResponse response;
	
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

	public PaidRefundResponse getResponse() {
		return response;
	}

	public void setResponse(PaidRefundResponse response) {
		this.response = response;
	}

	
}
