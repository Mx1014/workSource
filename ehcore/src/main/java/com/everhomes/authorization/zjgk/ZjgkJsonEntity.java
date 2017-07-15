package com.everhomes.authorization.zjgk;

public class ZjgkJsonEntity<T> {
	public static final int ERRORCODE_SUCCESS  = 200; // 错误码，成功为200，失败则为具体的非200错误码
	public static final int ERRORCODE_MISMATCHING  = 201; // 错误码201表示“提交信息不匹配”
	public static final int ERRORCODE_UNRENT  = 202; // 错误码202表示“用户已退租”
	public static final int ERRORCODE_SEND_REQUEST_EXCEPTION  = 500; // 错误码500表示“向张江高科系统查询，出现异常”
	public static final int ERRORCODE_NOT_PASS_IN_ZUOLIN  = 501; // 错误码501表示“在左邻系统中，用户所在小区未加入管理公司，不能通过认证。”
	
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
	
	public boolean isMismatching(){
		return errorCode == ERRORCODE_MISMATCHING;
	}
	
	public boolean isUnrent(){
		return errorCode == ERRORCODE_UNRENT;
	}
	
	public boolean isRequestFail(){
		return errorCode == ERRORCODE_SEND_REQUEST_EXCEPTION;
	}
	
	public boolean isNotPassInZuolin(){
		return errorCode == ERRORCODE_NOT_PASS_IN_ZUOLIN;
	}
}
