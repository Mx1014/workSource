// @formatter:off
package com.everhomes.express.guomao;

public class GuoMaoEMSResponseEntity<T> {
	private String requestno;
	private String count;
	private T mailnums;
	private String success;
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
	
}
