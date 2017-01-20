package com.everhomes.pmtask.ebei;

public class EbeiJsonEntity<T> {
	
	static final int SUCCESS = 1;
	static final int SUCCESS_CODE = 200;
	
	private Integer status;
	
	private Integer statusCode;
	
	private String message;
	
	private T data;

	public Boolean isSuccess() {
		if(statusCode == SUCCESS_CODE)
			return true;
		return false;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	
}
