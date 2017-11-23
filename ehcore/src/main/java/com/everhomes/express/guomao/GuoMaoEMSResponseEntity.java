// @formatter:off
package com.everhomes.express.guomao;

public class GuoMaoEMSResponseEntity<T> {
	private String success;
	private String errorMsg;
	private String errorCode;
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	
	public boolean isSuccess(){
		return "T".equals(success);
	}
	
}
