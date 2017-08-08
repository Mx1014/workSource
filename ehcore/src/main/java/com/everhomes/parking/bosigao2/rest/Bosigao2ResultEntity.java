package com.everhomes.parking.bosigao2.rest;

public class Bosigao2ResultEntity {
	private String errorCode;
	private String errorMsg;
	private Object result;
	
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	public boolean isSuccess(){
		return errorCode.equals("1");
	}
	
}	
