package com.everhomes.authorization.zjgk;

public class ZjgkJsonEntity<T> {
	public static final int ERRORCODE_SUCCESS  = 200;
	public static final int ERRORCODE_MISMATCHING  = 201;
	public static final int ERRORCODE_UNRENT  = 202;
	
	private String version;
	private String errorScope;
	private int errorCode;
	private String errorDescription;
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
	public T getResponse() {
		return response;
	}
	public void setResponse(T response) {
		this.response = response;
	}

	public boolean isSuccess(){
		return errorCode == ERRORCODE_SUCCESS;
	}
}
