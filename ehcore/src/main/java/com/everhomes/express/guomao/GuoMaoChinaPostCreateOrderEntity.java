// @formatter:off
package com.everhomes.express.guomao;

public class GuoMaoChinaPostCreateOrderEntity<T> {
	
	private String version;
	private String errorScope;
	private int errorCode;
	private String errorDescription;
	private String errorDetails;
	private T response;
	
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
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorDescription() {
		return errorDescription;
	}
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	public String getErrorDetails() {
		return errorDetails;
	}
	public void setErrorDetails(String errorDetails) {
		this.errorDetails = errorDetails;
	}
	public T getResponse() {
		return response;
	}
	public void setResponse(T response) {
		this.response = response;
	}

}
