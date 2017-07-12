package com.everhomes.authorization.zjgk;

public class ZjgkJsonEntity<T> {
	
	private String result;
	private T data;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public boolean isSuccess() {
		return "0".equals(this.result);
	}
}
