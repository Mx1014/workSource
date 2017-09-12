// @formatter:off
package com.everhomes.express.guomao.pay;

import com.everhomes.util.StringHelper;

public class PayResponse<T> {
	private Boolean success;
	private String content;
	private Integer errorCode;
	private Boolean result;
	private T data;
	private T body;
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public Boolean getResult() {
		return result;
	}
	public void setResult(Boolean result) {
		this.result = result;
	}
	public T getBody() {
		return body;
	}
	public void setBody(T body) {
		this.body = body;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
