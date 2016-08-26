package com.everhomes.statistics.transaction;

public class ListPaidTransactionResponse {

	private String version;
	
	private Integer errorCode;
	
	private PaidTransactionResponse response;
	
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

	public PaidTransactionResponse getResponse() {
		return response;
	}

	public void setResponse(PaidTransactionResponse response) {
		this.response = response;
	}

	
}
