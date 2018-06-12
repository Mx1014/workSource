package com.everhomes.parking.xiaomao;

import com.everhomes.util.StringHelper;

public class XiaomaoJsonEntity<T> {
	private Integer flag;
	private String message;
	private T data;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public boolean isSuccess() {
		return flag != null && flag == 1;
	}

}
