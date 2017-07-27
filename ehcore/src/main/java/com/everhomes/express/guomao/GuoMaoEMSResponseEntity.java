// @formatter:off
package com.everhomes.express.guomao;

public class GuoMaoEMSResponseEntity<T> {
	private String requestno;
	private String count;
	private T mailnums;
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
	public String getRequestno() {
		return requestno;
	}
	public void setRequestno(String requestno) {
		this.requestno = requestno;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public T getMailnums() {
		return mailnums;
	}
	public void setMailnums(T mailnums) {
		this.mailnums = mailnums;
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
