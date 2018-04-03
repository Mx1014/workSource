package com.everhomes.parking.xiaomao;

import com.everhomes.util.StringHelper;

public class XiaomaoJsonEntity {
	private Integer flag;
	private String message;

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
}
